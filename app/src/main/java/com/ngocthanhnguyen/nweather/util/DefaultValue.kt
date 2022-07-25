package com.ngocthanhnguyen.nweather.util

fun String?.defaultIfNull(): String {
    return this ?: ""
}

fun emptyString(): String {
    return ""
}