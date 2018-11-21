package io.oreon.tapad.analytics.repository;

import io.oreon.tapad.analytics.domain.Analytic;
import org.springframework.data.repository.CrudRepository;

public interface AnalyticRepository extends CrudRepository<Analytic, Long> {

    Analytic findByUser(String user);

    Iterable<Analytic> findByTimestampBeforeAndTimestampAfter(Long timestampBefore, Long timestampAfter);
}
