package com.ngocthanhnguyen.core.domain.usecase

import kotlinx.coroutines.flow.Flow

abstract class BaseUseCase<P, R> {
    abstract suspend fun execute(param: P): Flow<R>
}