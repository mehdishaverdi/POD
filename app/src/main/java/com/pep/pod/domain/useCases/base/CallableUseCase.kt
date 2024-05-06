package com.pep.pod.domain.useCases.base

abstract class CallableUseCase<T, P> {
    abstract suspend fun invoke(param: P) : T
}