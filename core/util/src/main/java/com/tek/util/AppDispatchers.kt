package com.tek.util

import kotlin.coroutines.CoroutineContext

@Suppress("PropertyName")
interface AppDispatchers {

    val Default: CoroutineContext

    val Main: CoroutineContext

    val IO: CoroutineContext

}