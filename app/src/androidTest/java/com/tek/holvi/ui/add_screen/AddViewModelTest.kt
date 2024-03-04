package com.tek.holvi.ui.add_screen

import com.google.common.truth.Truth.assertThat
import com.tek.database.dao.PasswordDao
import com.tek.database.model.Password
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock


class AddViewModelTest {

    private val addViewModel = AddViewModel(
        passwordDao = mock(PasswordDao::class.java)
    )

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
