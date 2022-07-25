package com.ngocthanhnguyen.nweather.util

import com.ngocthanhnguyen.nweather.di.appModule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

class DateTimeFormatterTest: KoinTest {

    private val dateTimeFormatter: DateTimeFormatter by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(appModule)
    }

    @Test
    fun epochToLocalDateTimePattern1() {
        assertEquals(
            dateTimeFormatter.epochToLocalDateTimePattern1(1658621400, -25200),
            "Sat, 23 Jul 2022"
        )
    }
}