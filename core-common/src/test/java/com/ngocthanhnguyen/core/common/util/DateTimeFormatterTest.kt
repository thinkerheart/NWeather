package com.ngocthanhnguyen.core.common.util

import com.ngocthanhnguyen.core.common.di.coreCommonModule
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

class DateTimeFormatterTest : KoinTest {

    private val dateTimeFormatter: DateTimeFormatter by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(coreCommonModule)
    }

    @Test
    fun epochToLocalDateTimePattern1() {
        assertEquals(
            dateTimeFormatter.epochToLocalDateTimePattern1(1658621400, -25200),
            "Sat, 23 Jul 2022"
        )
    }
}