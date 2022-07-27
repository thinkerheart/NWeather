package com.ngocthanhnguyen.core.common.di

import com.ngocthanhnguyen.core.common.util.DateTimeFormatter
import com.ngocthanhnguyen.core.common.util.DateTimeProvider
import org.koin.dsl.module

val coreCommonModule = module {
    single { DateTimeProvider() }
    single { DateTimeFormatter() }
}