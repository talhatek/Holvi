package com.tek.holvi.ui.add_screen


import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.tek.database.domain.AddPasswordUseCase
import com.tek.database.model.Password
import com.tek.password.domain.PasswordGeneratorUseCase
import com.tek.password.presentation.AddPasswordState
import com.tek.password.presentation.AddViewModel
import com.tek.test.HolviTestDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

@OptIn(ExperimentalCoroutinesApi::class)
class AddViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val testDispatcher = StandardTestDispatcher()

    private val addViewModel = AddViewModel(
        addPassword = mock(AddPasswordUseCase::class.java),
        passwordGenerator = PasswordGeneratorUseCase(),
        appDispatchers = HolviTestDispatchers(testDispatcher)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun generatePassword() = Password(
        siteName = "Site Name",
        password = "Password",
        userName = "User Name"
    )

    @Test
    fun addPassword() {
        runTest {
            addViewModel.addPassword(generatePassword())
            addViewModel.passwordAddState.test {
                assertThat(awaitItem()).isEqualTo(AddPasswordState.Success)

            }
        }

    }

    @Test
    fun addPasswordFailDueToPassword() {
        runTest {
            addViewModel.addPassword(generatePassword().copy(siteName = ""))
            addViewModel.passwordAddState.test {
                assertThat((awaitItem() as? AddPasswordState.Failure)?.message).isEqualTo("You must fill required fields.")
            }
        }
    }

    @Test
    fun generatePasswordSuccess() {
        runTest {
            addViewModel.generatePassword()
            addViewModel.passwordStateFlow.test {
                assertThat(awaitItem().length).isEqualTo(0)
                assertThat(awaitItem().length).isEqualTo(8)
            }
        }
    }
}
