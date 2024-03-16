package com.tek.holvi.ui.add_screen

import android.util.Log
import com.google.common.truth.Truth.assertThat
import com.tek.database.domain.AddPasswordUseCase
import com.tek.database.model.Password
import com.tek.password.domain.PasswordGeneratorUseCase
import com.tek.password.presentation.AddPasswordState
import com.tek.password.presentation.AddViewModel
import com.tek.test.HolviTestDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import kotlin.jvm.internal.Ref.IntRef

@OptIn(ExperimentalCoroutinesApi::class)
class AddViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val addViewModel = AddViewModel(
        addPassword = mock(AddPasswordUseCase::class.java),
        passwordGenerator = PasswordGeneratorUseCase(),
        appDispatchers = HolviTestDispatchers(testDispatcher)
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
        "Str".groupingBy { it }.foldTo(
            destination = mutableMapOf(),
            initialValueSelector = { key, item ->
                Log.e("initialValueSelector", "$key $item")
                IntRef()
            },
            operation = { key, accumulator, element ->
                accumulator.apply { this.element += 1 }

            }
        )
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


