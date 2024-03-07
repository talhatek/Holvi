package com.tek.util

import kotlin.coroutines.CoroutineContext

interface AppDispatchers {

    @Suppress("PropertyName")
    val Default: CoroutineContext

    @Suppress("PropertyName")
    val Main: CoroutineContext

    @Suppress("PropertyName")
    val IO: CoroutineContext

}