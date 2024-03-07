package com.tek.test

import com.tek.util.AppDispatchers
import kotlinx.coroutines.test.TestDispatcher
import kotlin.coroutines.CoroutineContext

class HolviTestDispatchers(private val testDispatcher: TestDispatcher) : AppDispatchers {
    override val Default: CoroutineContext
        get() = testDispatcher
    override val Main: CoroutineContext
        get() = testDispatcher
    override val IO: CoroutineContext
        get() = testDispatcher

}
