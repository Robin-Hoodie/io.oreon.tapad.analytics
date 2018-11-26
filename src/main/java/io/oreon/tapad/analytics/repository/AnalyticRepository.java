package io.oreon.tapad.analytics.repository;

import io.oreon.tapad.analytics.domain.Analytic;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface AnalyticRepository extends CrudRepository<Analytic, Long> {

    <S extends Analytic> S save(S entity);

    Collection<Analytic> findByTimestampBetween(Long fromTimestamp, Long toTimestamp);
}
