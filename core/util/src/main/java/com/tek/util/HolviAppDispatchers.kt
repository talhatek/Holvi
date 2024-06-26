package com.tek.util

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Suppress("VARIABLE_NAME_INCORRECT_FORMAT")
class HolviAppDispatchers : AppDispatchers {
    override val Default: CoroutineContext
        get() = Dispatchers.Default
    override val Main: CoroutineContext
        get() = Dispatchers.Main
    override val IO: CoroutineContext
        get() = Dispatchers.IO

}
