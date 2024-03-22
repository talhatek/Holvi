package com.tek.password.presentation


import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
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
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
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
    private lateinit var crudViewModel: CrudViewModel

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
        crudViewModel = CrudViewModel(
            getPasswordBySiteName = getPasswordBySiteNameUseCase,
            addPassword = addPasswordUseCase,
            updatePassword = updatePasswordUseCase,
            passwordGenerator = passwordGeneratorUseCase,
            deletePassword = deletePasswordUseCase,
            observePassword = observePasswordUseCase,
            appDispatchers = appDispatchers,
            observeOnStart = true
        )
    }


    private fun generatePassword() = Password(
        id = 0,
        siteName = "Site Name",
        password = "Password",
        userName = "User Name"
    )

    @Test
    fun `add password successfully`() {
        runTest {
            coEvery { addPasswordUseCase.invoke(generatePassword()) } just runs
            crudViewModel.passwordAddState.test {
                crudViewModel.add(generatePassword())
                coVerify(exactly = 1) { addPasswordUseCase.invoke(generatePassword()) }
                assertThat(awaitItem()).isEqualTo(AddPasswordState.Success)
            }
        }
    }

    @Test
    fun `add password fails, due to empty site name`() {
        runTest {
            crudViewModel.passwordAddState.test {
                crudViewModel.add(generatePassword().copy(siteName = ""))
                assertThat((awaitItem() as? AddPasswordState.Failure)?.message).isEqualTo("You must fill required fields.")
            }
        }
    }
    @Test
    fun `update password successfully`() {
        runTest {
            coEvery { updatePasswordUseCase.invoke(generatePassword()) } just runs
            crudViewModel.passwordAddState.test {
                crudViewModel.update(generatePassword())
                coVerify(exactly = 1) { updatePasswordUseCase.invoke(generatePassword()) }
                assertThat(awaitItem()).isEqualTo(AddPasswordState.Success)
            }
        }
    }

    @Test
    fun `update password fails`() {
        runTest {
            crudViewModel.passwordAddState.test {
                crudViewModel.update(generatePassword().copy(siteName = ""))
                assertThat((awaitItem() as? AddPasswordState.Failure)?.message).isEqualTo("You must fill required fields.")
            }
        }
    }

    @Test
    fun `delete password successfully`() {
        runTest {
            coEvery { getPasswordBySiteNameUseCase.invoke(generatePassword().id) } returns generatePassword()
            coEvery { deletePasswordUseCase.invoke(generatePassword()) } returns 1
            crudViewModel.passwordDeleteState.test {
                crudViewModel.delete(generatePassword().id)
                coVerify(exactly = 1) { getPasswordBySiteNameUseCase.invoke(generatePassword().id) }
                coVerify(exactly = 1) { deletePasswordUseCase.invoke(generatePassword()) }
                assertThat(awaitItem()).isEqualTo(DeletePasswordState.Success)
            }
        }
    }

    @Test
    fun `delete password not found`() {
        runTest {
            coEvery { getPasswordBySiteNameUseCase.invoke(generatePassword().id) } returns generatePassword()
            coEvery { deletePasswordUseCase.invoke(generatePassword()) } returns 0
            crudViewModel.passwordDeleteState.test {
                crudViewModel.delete(generatePassword().id)
                coVerify(exactly = 1) { getPasswordBySiteNameUseCase.invoke(generatePassword().id) }
                coVerify(exactly = 1) { deletePasswordUseCase.invoke(generatePassword()) }
                assertThat(awaitItem()).isEqualTo(DeletePasswordState.NotFound)
            }
        }
    }

    @Test
    fun `delete password  getPasswordBySiteNameUseCase throw exception`() {
        runTest {
            coEvery { getPasswordBySiteNameUseCase.invoke(generatePassword().id) }.throws(Exception())
            crudViewModel.passwordDeleteState.test {
                crudViewModel.delete(generatePassword().id)
                coVerify(exactly = 1) { getPasswordBySiteNameUseCase.invoke(generatePassword().id) }
                assertThat(awaitItem()).isEqualTo(DeletePasswordState.Failure)
            }
        }
    }

    @Test
    fun `delete password  deletePasswordUseCase throw exception`() {
        runTest {
            coEvery { getPasswordBySiteNameUseCase.invoke(generatePassword().id) } returns generatePassword()
            coEvery { deletePasswordUseCase.invoke(generatePassword()) }.throws(Exception())
            crudViewModel.passwordDeleteState.test {
                crudViewModel.delete(generatePassword().id)
                coVerify(exactly = 1) { getPasswordBySiteNameUseCase.invoke(generatePassword().id) }
                coVerify(exactly = 1) { deletePasswordUseCase.invoke(generatePassword()) }
                assertThat(awaitItem()).isEqualTo(DeletePasswordState.Failure)
            }
        }
    }

    @Test
    fun `undo delete successfully`() {
        runTest {
            coEvery { getPasswordBySiteNameUseCase.invoke(generatePassword().id) } returns generatePassword()
            coEvery { deletePasswordUseCase.invoke(generatePassword()) } returns 1
            coEvery { addPasswordUseCase.invoke(generatePassword()) } just runs
            crudViewModel.passwordDeleteState.test {
                crudViewModel.delete(generatePassword().id)
                coVerify(exactly = 1) { getPasswordBySiteNameUseCase.invoke(generatePassword().id) }
                coVerify(exactly = 1) { deletePasswordUseCase.invoke(generatePassword()) }
                assertThat(awaitItem()).isEqualTo(DeletePasswordState.Success)
                crudViewModel.undoDelete()
                assertThat(awaitItem()).isEqualTo(DeletePasswordState.Undo)
            }
        }
    }

    @Test
    fun `undo delete Failure after 4 sec`() {
        runTest {
            coEvery { getPasswordBySiteNameUseCase.invoke(generatePassword().id) } returns generatePassword()
            coEvery { deletePasswordUseCase.invoke(generatePassword()) } returns 1
            coEvery { addPasswordUseCase.invoke(generatePassword()) } just runs
            crudViewModel.passwordDeleteState.test {
                crudViewModel.delete(generatePassword().id)
                coVerify(exactly = 1) { getPasswordBySiteNameUseCase.invoke(generatePassword().id) }
                coVerify(exactly = 1) { deletePasswordUseCase.invoke(generatePassword()) }
                assertThat(awaitItem()).isEqualTo(DeletePasswordState.Success)
                testDispatchers.scheduler.advanceTimeBy(4001)
                crudViewModel.undoDelete()
                assertThat(awaitItem()).isEqualTo(DeletePasswordState.Failure)
            }
        }
    }


    @Test
    fun `generates password successfully`() {
        every {
            passwordGeneratorUseCase.invoke(
                isWithLowercase = true,
                isWithUppercase = true,
                isWithNumbers = true,
                isWithSpecial = true,
                length = 8,
                forbiddenChars = charArrayOf()
            )
        } returns "aA44!^bV"
        runTest {
            crudViewModel.passwordStateFlow.test {
                assertThat(awaitItem().length).isEqualTo(0)
                crudViewModel.generate()
                verify(exactly = 1) {
                    passwordGeneratorUseCase(any(), any(), any(), any(), any(), any())
                }
                assertThat(awaitItem().length).isEqualTo(8)
            }
        }
    }

    @Test
    fun `updates query successfully`() {
        runTest {
            crudViewModel.updateQuery("aa")
            crudViewModel.queryFlow.test {
                assertThat(awaitItem()).isEqualTo("aa")

            }
        }
    }

    @Test
    fun `observes passwords successfully, returns empty list`() {
        runTest {
            every { observePasswordUseCase.invoke("") } returns flowOf(emptyList())
            crudViewModel.passwordsState.test {
                assertThat(awaitItem()).isEqualTo(PasswordsState.Init)
                testDispatchers.scheduler.advanceTimeBy(251L)
                assertThat(awaitItem()).isEqualTo(PasswordsState.Loading)
                val item = awaitItem() as PasswordsState.Success
                assertThat(item.isEmpty).isEqualTo(true)
                assertThat(item.isQueried).isEqualTo(false)
                assertThat(item.data.size).isEqualTo(0)
            }
        }
    }

    @Test
    fun `observes passwords successfully, returns list`() {
        runTest {
            every { observePasswordUseCase.invoke("") } returns flowOf(listOf(generatePassword()))
            crudViewModel.passwordsState.test {
                assertThat(awaitItem()).isEqualTo(PasswordsState.Init)
                testDispatchers.scheduler.advanceTimeBy(251L)
                assertThat(awaitItem()).isEqualTo(PasswordsState.Loading)
                val item = awaitItem() as PasswordsState.Success
                assertThat(item.isEmpty).isEqualTo(false)
                assertThat(item.data.size).isEqualTo(1)
                assertThat(item.isQueried).isEqualTo(false)
            }
        }
    }

    @Test
    fun `observes passwords throws exception`() {
        runTest {
            every { observePasswordUseCase.invoke("") }.throws(Exception())
            crudViewModel.passwordsState.test {
                assertThat(awaitItem()).isEqualTo(PasswordsState.Init)
                testDispatchers.scheduler.advanceTimeBy(251L)
                assertThat(awaitItem()).isEqualTo(PasswordsState.Loading)
                val item = awaitItem() as PasswordsState.Error
                assertThat(item.message).isEqualTo("Something occurred!")
            }
        }
    }
}
