package com.tek.util

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class HolviAppDispatchers : AppDispatchers {
    override val Default: CoroutineContext
        get() = Dispatchers.Default
    override val Main: CoroutineContext
        get() = Dispatchers.Main
    override val IO: CoroutineContext
        get() = Dispatchers.IO

}
