package com.pep.pod.domain.useCases.base

abstract class UseCase<T, P> {
    abstract fun invoke(param: P) : T
}