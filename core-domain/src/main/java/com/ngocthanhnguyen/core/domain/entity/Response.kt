package com.ngocthanhnguyen.core.domain.entity

sealed class Response<T> {
    class Uninitialized<T> : Response<T>()
    class Loading<T> : Response<T>()
    data class Success<T>(val data: T) : Response<T>()
    data class Error<T>(val errorValue: String) : Response<T>()
}