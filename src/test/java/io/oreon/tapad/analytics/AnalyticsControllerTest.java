package io.oreon.tapad.analytics;

import io.oreon.tapad.analytics.domain.Analytic;
import io.oreon.tapad.analytics.repository.AnalyticRepository;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TapadAnalyticsApplication.class, webEnvironment = RANDOM_PORT)
public class AnalyticsControllerTest {

    @LocalServerPort
    private int port;

    @Inject
    private AnalyticRepository analyticRepository;

    @Before
    public void setupRestAssured() {
        RestAssured.port = port;
    }

    @After
    public void cleanCrudRepositories() {
        analyticRepository.deleteAll();
    }

    @Test
    public void shouldSaveAClickAnalytic() {
        given()
                .when()
                .post("/analytics?timestamp=100&user=Robin&click=true")
                .then()
                .statusCode(SC_CREATED);

        assertThat(this.analyticRepository.findAll())
                .hasSize(1)
                .extracting(Analytic::isClick)
                .first()
                .isEqualTo(true);
    }

    @Test
    public void shouldSaveAnImpressionAnalytic() {
        given()
                .when()
                .post("/analytics?timestamp=100&user=Robin&impression=true")
                .then()
                .statusCode(SC_CREATED);

        assertThat(this.analyticRepository.findAll())
                .hasSize(1)
                .extracting(Analytic::isImpression)
                .first()
                .isEqualTo(true);
    }

}
