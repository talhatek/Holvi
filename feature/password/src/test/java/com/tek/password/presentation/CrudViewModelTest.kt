package com.tek.password.presentation


import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.tek.database.data.PasswordDto
import com.tek.database.domain.AddPasswordUseCase
import com.tek.database.domain.DeletePasswordUseCase
import com.tek.database.domain.GetPasswordBySiteNameUseCase
import com.tek.database.domain.ObservePasswordUseCase
import com.tek.database.domain.UpdatePasswordUseCase
import com.tek.database.domain.mapper.PasswordDtoToPasswordMapper
import com.tek.database.model.Password
import com.tek.password.domain.PasswordGeneratorUseCase
import com.tek.test.HolviTestDispatchers
import com.tek.util.AppDispatchers
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CrudViewModelTest {

    private lateinit var passwordDtoToPasswordMapper: PasswordDtoToPasswordMapper

    private lateinit var addPasswordUseCase: AddPasswordUseCase


    private lateinit var updatePasswordUseCase: UpdatePasswordUseCase


    private lateinit var deletePasswordUseCase: DeletePasswordUseCase


    private lateinit var getPasswordBySiteNameUseCase: GetPasswordBySiteNameUseCase


    private lateinit var observePasswordUseCase: ObservePasswordUseCase


    private lateinit var passwordGeneratorUseCase: PasswordGeneratorUseCase

    private lateinit var appDispatchers: AppDispatchers

    private lateinit var vm: CrudViewModel


    private val testDispatchers = UnconfinedTestDispatcher()


    @Before
    fun setup() {
        appDispatchers = HolviTestDispatchers(testDispatchers)
        passwordDtoToPasswordMapper = mockk()
        addPasswordUseCase = mockk()
        getPasswordBySiteNameUseCase = mockk()
        updatePasswordUseCase = mockk()
        deletePasswordUseCase = mockk()
        observePasswordUseCase = mockk()
        passwordGeneratorUseCase = mockk()
        vm = CrudViewModel(
            getPasswordBySiteNameUseCase,
            addPasswordUseCase,
            updatePasswordUseCase,
            passwordGeneratorUseCase,
            deletePasswordUseCase,
            observePasswordUseCase,
            appDispatchers,
            true
        )
    }


    private fun generatePassword() = Password(
        id = 0,
        siteName = "Site Name",
        password = "Password",
        userName = "User Name"
    )

    private fun generatePasswordDto() = PasswordDto(
        id = 0,
        siteName = "Site Name",
        password = "Password",
        userName = "User Name"
    )

    @Test
    fun addPassword_Success() {
        runTest {
            coEvery { addPasswordUseCase.invoke(generatePassword()) } just runs
            vm.passwordAddState.test {
                vm.add(generatePassword())
                assertThat(awaitItem()).isEqualTo(AddPasswordState.Success)
            }
        }
    }

    @Test
    fun addPassword_FailsWhenSiteNameIsEmpty() {
        runTest {
            vm.passwordAddState.test {
                vm.add(generatePassword().copy(siteName = ""))
                assertThat((awaitItem() as? AddPasswordState.Failure)?.message).isEqualTo("You must fill required fields.")
            }
        }
    }

    @Test
    fun generatePassword_InitiallyEmptyThenGeneratesEightCharacters() {
        every {
            passwordGeneratorUseCase.invoke(
                true,
                true,
                true,
                true,
                8,
                charArrayOf()
            )
        } returns "aA44!^bv"
        runTest {
            vm.passwordStateFlow.test {
                vm.generate()
                assertThat(awaitItem().length).isEqualTo(0)
                assertThat(awaitItem().length).isEqualTo(8)
            }
        }
    }

    @Test
    fun observePasswordsEmpty() {
        runTest {
            every { observePasswordUseCase.invoke("") } returns flowOf(emptyList())
            vm.passwordsState.test {
                assertThat(awaitItem()).isEqualTo(PasswordsState.Init)
                testDispatchers.scheduler.advanceTimeBy(251L)
                val item = awaitItem() as PasswordsState.Success
                assertThat(item.isEmpty).isEqualTo(true)
                assertThat(item.isQueried).isEqualTo(false)
                assertThat(item.data.size).isEqualTo(0)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun observePasswordsItem() {
        runTest {
            every { observePasswordUseCase.invoke("") } returns flowOf(listOf(generatePassword()))
            vm.passwordsState.test {
                assertThat(awaitItem()).isEqualTo(PasswordsState.Init)
                testDispatchers.scheduler.advanceTimeBy(251L)
                val item = awaitItem() as PasswordsState.Success
                assertThat(item.isEmpty).isEqualTo(false)
                assertThat(item.data.size).isEqualTo(1)
                assertThat(item.isQueried).isEqualTo(false)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}
