package com.tek.holvi.ui.add_screen

import com.google.common.truth.Truth.assertThat
import com.tek.database.domain.AddPasswordUseCase
import com.tek.database.domain.DeletePasswordUseCase
import com.tek.database.domain.GetPasswordBySiteNameUseCase
import com.tek.database.domain.ObservePasswordUseCase
import com.tek.database.domain.SearchPasswordUseCase
import com.tek.database.domain.UpdatePasswordUseCase
import com.tek.database.model.Password
import com.tek.password.domain.PasswordGeneratorUseCase
import com.tek.password.presentation.AddPasswordState
import com.tek.password.presentation.CrudViewModel
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

@OptIn(ExperimentalCoroutinesApi::class)
class CrudViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val crudViewModel = CrudViewModel(
        addPassword = mock(AddPasswordUseCase::class.java),
        updatePassword = mock(UpdatePasswordUseCase::class.java),
        searchPassword = mock(SearchPasswordUseCase::class.java),
        deletePassword = mock(DeletePasswordUseCase::class.java),
        getPasswordBySiteName = mock(GetPasswordBySiteNameUseCase::class.java),
        observePassword = mock(ObservePasswordUseCase::class.java),
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
        runTest {
            crudViewModel.add(generatePassword())
            assertThat(crudViewModel.passwordAddState.first()).isEqualTo(AddPasswordState.Success)
        }
    }

    @Test
    fun addPasswordFailDueToPassword() {
        runTest {
            crudViewModel.add(generatePassword().copy(siteName = ""))
            val state = crudViewModel.passwordAddState.first()
            assertThat(state).isInstanceOf(AddPasswordState.Failure::class.java)
            assertThat((state as AddPasswordState.Failure).message).isEqualTo("You must fill required fields.")
        }
    }
}


