package io.oreon.tapad.analytics.service;

import io.oreon.tapad.analytics.cache.AnalyticsCache;
import io.oreon.tapad.analytics.domain.Analytic;
import io.oreon.tapad.analytics.repository.AnalyticRepository;
import org.springframework.cache.annotation.CacheConfig;

import javax.inject.Named;
import java.util.Collection;
import java.util.function.Predicate;

import static io.oreon.tapad.analytics.domain.Analytic.Builder.anAnalytic;

@Named
@CacheConfig
public class AnalyticService {

    private AnalyticRepository analyticRepository;
    private TimeService timeService;
    private AnalyticsCache analyticsCache;

    public AnalyticService(AnalyticRepository analyticRepository, TimeService timeService, AnalyticsCache analyticsCache) {
        this.analyticRepository = analyticRepository;
        this.timeService = timeService;
        this.analyticsCache = analyticsCache;
    }

    //Retrieve from cache if timestamp is for the current hour, otherwise retrieve from the database
    public Collection<Analytic> findAnalyticsByHourOfTimestamp(Long timestamp) {
        Long fromTimestamp = timeService.floorTimestampToHour(timestamp);
        Long toTimestamp = timeService.ceilTimestampToHour(timestamp);
        if (this.timeService.isCurrentHourTimestamp(timestamp)) {
            return this.analyticsCache.getAnalyticsForHour(fromTimestamp, toTimestamp);
        }
        return this.analyticRepository.findByTimestampBetween(fromTimestamp, toTimestamp);
    }

    //Save to DB & optionally to Cache if timestamp is for the current hour
    public void saveAnalytic(String user, Long timestamp, boolean click, boolean impression) {
        if (click && impression) {
            throw new IllegalArgumentException("An analytic can't be for both a click and an impression");
        }
        if (click || impression) {
            Analytic analytic = anAnalytic()
                    .withUser(user)
                    .withTimestamp(timestamp)
                    .withClick(click)
                    .withImpression(impression)
                    .build();
            if (this.timeService.isCurrentHourTimestamp(timestamp)) {
                Long fromTimestamp = timeService.floorTimestampToHour(timestamp);
                Long toTimestamp = timeService.ceilTimestampToHour(timestamp);
                Collection<Analytic> analyticsForHour = this.analyticsCache.getAnalyticsForHour(fromTimestamp, toTimestamp);
                this.analyticsCache.updateAnalyticsForHour(fromTimestamp, toTimestamp, analyticsForHour, analytic);
            }
            this.analyticRepository.save(analytic);
        } else {
            throw new IllegalArgumentException("An analytic should be for either a click or an impression");
        }
    }

    public String analyticsResponse(Collection<Analytic> analytics) {
        return String.format("unique_users,%d\nclicks,%d\nimpressions,%d\n",
                countOfUniqueUsers(analytics),
                countOfProperty(analytics, Analytic::isClick),
                countOfProperty(analytics, Analytic::isImpression));
    }

    public Long countOfUniqueUsers(Collection<Analytic> analytics) {
        return analytics
                .stream()
                .map(Analytic::getUser)
                .distinct()
                .count();
    }

    public Long countOfProperty(Collection<Analytic> analytics, Predicate<Analytic> propertyPredicate) {
        return analytics
                .stream()
                .filter(propertyPredicate)
                .count();
    }
}
