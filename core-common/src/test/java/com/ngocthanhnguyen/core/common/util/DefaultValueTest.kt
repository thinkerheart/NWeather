package com.ngocthanhnguyen.core.common.util

import org.junit.Assert.*

import org.junit.Test

class DefaultValueTest {

    @Test
    fun defaultIfNullString() {
        var str: String? = null
        assertEquals(str.defaultIfNull(), "")
        str = "not null"
        assertEquals(str.defaultIfNull(), str)
    }

    @Test
    fun defaultIfNullLong() {
        var numL: Long? = null
        assertEquals(numL.defaultIfNull(), 0)
        numL = 1
        assertEquals(numL.defaultIfNull(), 1)
    }

    @Test
    fun defaultIfNullDouble() {
        var numD: Double? = null
        assertTrue(numD.defaultIfNull() == 0.0)
        numD = 1.0
        assertTrue(numD.defaultIfNull() == 1.0)
    }

    @Test
    fun emptyString_is_empty() {
        assertEquals(emptyString(), "")
    }
}