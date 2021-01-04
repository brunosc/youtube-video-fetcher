package com.github.brunosc.fetcher.domain;

import com.google.api.client.util.DateTime;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

class DateTimeConverter {

    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

    private DateTimeConverter(){}

    static LocalDateTime toLocalDateTime(DateTime dateTime) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(dateTime.getValue()), GMT.toZoneId());
    }

}
