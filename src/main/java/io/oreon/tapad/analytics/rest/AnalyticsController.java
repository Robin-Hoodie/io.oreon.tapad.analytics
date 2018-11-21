package io.oreon.tapad.analytics.rest;

import io.oreon.tapad.analytics.domain.Analytic;
import io.oreon.tapad.analytics.service.AnalyticService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URISyntaxException;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.util.MimeTypeUtils.TEXT_PLAIN_VALUE;

@Controller
@RequestMapping("/analytics")
public class AnalyticsController {

    private AnalyticService analyticService;

    public AnalyticsController(AnalyticService analyticService) {
        this.analyticService = analyticService;
    }

    @PostMapping
    public ResponseEntity<?> index(@RequestParam(value = "timestamp") Long timestamp,
                                   @RequestParam(value = "user") String user,
                                   @RequestParam(value = "click", defaultValue = "false") Boolean click,
                                   @RequestParam(value = "impression", defaultValue = "false") Boolean impression) {
        this.analyticService.saveAnalytic(user, timestamp, click, impression);
        return status(CREATED).build();
    }

    @GetMapping(produces = TEXT_PLAIN_VALUE)
    public ResponseEntity<Analytic> get(@RequestParam("timestamp") Long timestamp) {
        return null;
    }
}
