package io.oreon.tapad.analytics.service;

import io.oreon.tapad.analytics.domain.Analytic;
import io.oreon.tapad.analytics.repository.AnalyticRepository;

import javax.inject.Named;
import javax.transaction.Transactional;

import static io.oreon.tapad.analytics.domain.Analytic.Builder.anAnalytic;

@Named
public class AnalyticService {

    private AnalyticRepository analyticRepository;

    public AnalyticService(AnalyticRepository analyticRepository) {
        this.analyticRepository = analyticRepository;
    }

    public Analytic findByUser(String user) {
        return this.analyticRepository.findByUser(user);
    }

    public void saveAnalytic(String user, Long timestamp, boolean click, boolean impression) {
        if (click && impression) {
            throw new IllegalArgumentException("An analytic can't be for both a click and an impression");
        }
        if (click) {
            this.saveClickAnalytic(user, timestamp);
        } else if (impression) {
            this.saveImpressionAnalytic(user, timestamp);
        } else {
            throw new IllegalArgumentException("An analytic should be for either a click or an impression");
        }
    }

    private void saveClickAnalytic(String user, Long timestamp) {
        analyticRepository.save(anAnalytic()
                .withUser(user)
                .withTimestamp(timestamp)
                .withClick()
                .build());
    }

    private void saveImpressionAnalytic(String user, Long timestamp) {
        analyticRepository.save(anAnalytic()
                .withUser(user)
                .withTimestamp(timestamp)
                .withImpression()
                .build());
    }
}
