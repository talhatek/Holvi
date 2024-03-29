package com.tek.util

import kotlin.coroutines.CoroutineContext

@Suppress("PropertyName", "VARIABLE_NAME_INCORRECT_FORMAT")
interface AppDispatchers {
    val Default: CoroutineContext
    val Main: CoroutineContext
    val IO: CoroutineContext
}