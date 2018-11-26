package io.oreon.tapad.analytics.service;

import javax.inject.Named;
import java.time.LocalDateTime;

import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneId.of;
import static java.time.ZoneOffset.UTC;
import static java.time.temporal.ChronoUnit.HOURS;

@Named
public class TimeService {

    public boolean isCurrentHourTimestamp(Long timestamp) {
        return floorTimestampToHour(System.currentTimeMillis()).equals(floorTimestampToHour(timestamp));
    }

    public Long floorTimestampToHour(Long timestamp) {
        LocalDateTime dateTime = timestampToDateTime(timestamp);
        return dateTime.truncatedTo(HOURS).toInstant(UTC).toEpochMilli();
    }

    public Long ceilTimestampToHour(Long timestamp) {
        LocalDateTime dateTime = timestampToDateTime(timestamp);
        return dateTime.truncatedTo(HOURS).plusHours(1).toInstant(UTC).toEpochMilli();
    }

    private LocalDateTime timestampToDateTime(Long timestamp) {
        return ofInstant(ofEpochMilli(timestamp), of("UTC"));
    }
}
