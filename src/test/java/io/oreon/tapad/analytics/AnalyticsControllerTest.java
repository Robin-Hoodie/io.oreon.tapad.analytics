package io.oreon.tapad.analytics;

import io.oreon.tapad.analytics.cache.AnalyticsCache;
import io.oreon.tapad.analytics.domain.Analytic;
import io.oreon.tapad.analytics.repository.AnalyticRepository;
import io.oreon.tapad.analytics.service.AnalyticService;
import io.oreon.tapad.analytics.service.TimeService;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static io.oreon.tapad.analytics.domain.Analytic.Builder.anAnalytic;
import static io.restassured.RestAssured.given;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TapadAnalyticsApplication.class, webEnvironment = RANDOM_PORT)
public class AnalyticsControllerTest {

    @LocalServerPort
    private int port;

    @Mock
    private AnalyticsCache analyticsCache;

    @Mock
    private AnalyticRepository analyticRepository;

    @Inject
    @InjectMocks
    private AnalyticService analyticService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RestAssured.port = port;
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }

    @Test
    public void shouldSaveAClickAnalytic() {
        given()
                .when()
                .post("/analytics?timestamp=100&user=Robin&click=true")
                .then()
                .statusCode(SC_CREATED);

        ArgumentCaptor<Analytic> argumentCaptor = ArgumentCaptor.forClass(Analytic.class);
        verify(this.analyticRepository, times(1)).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().isClick()).isTrue();
    }

    @Test
    public void shouldSaveAnImpressionAnalytic() {
        given()
                .when()
                .post("/analytics?timestamp=100&user=Robin&impression=true")
                .then()
                .statusCode(SC_CREATED);

        ArgumentCaptor<Analytic> argumentCaptor = ArgumentCaptor.forClass(Analytic.class);
        verify(this.analyticRepository, times(1)).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().isImpression()).isTrue();
    }

    @Test
    public void shouldGetAnalyticBetweenTimestamps() {
        given()
                .when()
                .get("/analytics?timestamp=3600001")
                .then()
                .statusCode(SC_OK);

        verify(this.analyticRepository, times(1)).findByTimestampBetween(3600000L, 7200000L);
    }

    @Test
    public void shouldBeSavedToCacheAndRepository() {
        Long now = System.currentTimeMillis();
        given()
                .when()
                .post("/analytics?timestamp={timestamp}&user=Robin&click=true", now)
                .then()
                .statusCode(SC_CREATED);

        verify(analyticsCache, times(1)).updateAnalyticsForHour(anyLong(), anyLong(), anyCollection(), any(Analytic.class));
        verify(analyticRepository, times(1)).save(any(Analytic.class));
    }

    @Test
    public void shouldBeSavedOnlyToRepository() {
        Long epoch = 0L;
        given()
                .when()
                .post("/analytics?timestamp={timestamp}&user=Robin&click=true", epoch)
                .then()
                .statusCode(SC_CREATED);

        verify(analyticsCache, never()).updateAnalyticsForHour(anyLong(), anyLong(), anyCollection(), any(Analytic.class));
        verify(analyticRepository, times(1)).save(any(Analytic.class));
    }

    @Test
    public void shouldBeRetrievedFromCache() {
        given()
                .when()
                .get("/analytics?timestamp={timestamp}", System.currentTimeMillis())
                .then()
                .statusCode(SC_OK);

        verify(analyticsCache, times(1)).getAnalyticsForHour(anyLong(), anyLong());
        verify(analyticRepository, never()).findByTimestampBetween(anyLong(), anyLong());
    }

    @Test
    public void shouldBeRetrievedFromRepository() {

        given()
                .when()
                .get("/analytics?timestamp={timestamp}", 0L)
                .then()
                .statusCode(SC_OK);

        verify(analyticsCache, never()).getAnalyticsForHour(anyLong(), anyLong());
        verify(analyticRepository, times(1)).findByTimestampBetween(anyLong(), anyLong());
    }

}
