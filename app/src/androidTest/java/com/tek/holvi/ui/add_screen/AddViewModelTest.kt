package com.tek.holvi.ui.add_screen

import com.google.common.truth.Truth.assertThat
import com.tek.database.dao.PasswordDao
import com.tek.database.model.Password
import com.tek.password.domain.PasswordGeneratorUseCase
import com.tek.password.presentation.AddPasswordState
import com.tek.password.presentation.AddViewModel
import com.tek.password.presentation.AppDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalCoroutinesApi::class)
class AddViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val addViewModel = AddViewModel(
        passwordDao = mock(PasswordDao::class.java),
        passwordGenerator = PasswordGeneratorUseCase(),
        appDispatchers = DefaultTestDispatchers(testDispatcher)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun generatePassword() = Password(
        id = 0,
        siteName = "Site Name",
        password = "Password",
        userName = "User Name"
    )

    @Test
    fun addPassword() {
        runTest {
            addViewModel.addPassword(generatePassword())
            assertThat(addViewModel.passwordAddState.first()).isEqualTo(AddPasswordState.Success)
        }
    }

    @Test
    fun addPasswordFailDueToPassword() {
        runTest {
            addViewModel.addPassword(generatePassword().copy(siteName = ""))
            val state = addViewModel.passwordAddState.first()
            assertThat(state).isInstanceOf(AddPasswordState.Failure::class.java)
            assertThat((state as AddPasswordState.Failure).message).isEqualTo("You must fill required fields.")
        }
    }
}

class DefaultTestDispatchers(private val testDispatcher: TestDispatcher) : AppDispatchers {
    override val Default: CoroutineContext
        get() = testDispatcher
    override val Main: CoroutineContext
        get() = testDispatcher
    override val IO: CoroutineContext
        get() = testDispatcher

}


