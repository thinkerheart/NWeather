package com.ngocthanhnguyen.nweather.util

import org.junit.Assert.*

import org.junit.Test

class DefaultValueTest {

    @Test
    fun defaultIfNull() {
        var str: String? = null
        assertEquals(str.defaultIfNull(), "")
        str = "not null"
        assertEquals(str.defaultIfNull(), str)
    }

    @Test
    fun emptyString_is_empty() {
        assertEquals(emptyString(), "")
    }
}