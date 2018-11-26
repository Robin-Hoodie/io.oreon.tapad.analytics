package io.oreon.tapad.analytics.cache;

import io.oreon.tapad.analytics.domain.Analytic;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;

/*
 * Analytics for current hour are saved and retrieved from cache
 * Cache expires after 1 hour (see resources/application.yml), so @CacheEvict logic is not necessary
 */
@Named
@CacheConfig(cacheNames = "analytics")
public class AnalyticsCache {

    @Cacheable(key = "#fromTimestamp + '' + #toTimestamp")
    public Collection<Analytic> getAnalyticsForHour(Long fromTimestamp, Long toTimestamp) {
        return new ArrayList<>();
    }

    @CachePut(key = "#fromTimestamp + '' + #toTimestamp")
    public Collection<Analytic> updateAnalyticsForHour(Long fromTimestamp, Long toTimestamp, Collection<Analytic> analytics, Analytic newAnalytic) {
        analytics.add(newAnalytic);
        return analytics;
    }
}
