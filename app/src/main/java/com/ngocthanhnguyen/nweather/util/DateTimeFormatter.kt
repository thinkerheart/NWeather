package com.ngocthanhnguyen.nweather.util

import com.ngocthanhnguyen.nweather.enum.DateTimeFormatPattern
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class DateTimeFormatter {
    fun epochToLocalDateTimePattern1(epochTime: Long, timeZone: Long): String {
        val formatter = DateTimeFormatter.ofPattern(DateTimeFormatPattern.PATTERN1.pattern)
        return Instant.ofEpochSecond(epochTime)
            .atOffset(ZoneOffset.ofTotalSeconds(timeZone.toInt()))
            .toLocalDateTime()
            .format(formatter)
    }
}