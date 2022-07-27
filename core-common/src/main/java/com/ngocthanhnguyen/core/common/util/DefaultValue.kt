package com.ngocthanhnguyen.core.common.util

fun String?.defaultIfNull(): String {
    return this ?: ""
}

fun Double?.defaultIfNull(): Double {
    return this ?: 0.0
}

fun Long?.defaultIfNull(): Long {
    return this ?: 0
}

fun emptyString(): String {
    return ""
}