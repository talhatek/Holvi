package com.tek.password.presentation


import app.cash.turbine.test
import com.tek.database.domain.AddPasswordUseCase
import com.tek.database.domain.DeletePasswordUseCase
import com.tek.database.domain.GetPasswordBySiteNameUseCase
import com.tek.database.domain.PagingPasswordUseCase
import com.tek.database.domain.UpdatePasswordUseCase
import com.tek.database.domain.mapper.PasswordDtoToPasswordMapper
import com.tek.database.model.Password
import com.tek.password.domain.PasswordGeneratorUseCase
import com.tek.test.HolviTestDispatchers
import com.tek.util.AppDispatchers
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CrudViewModelTest {

    private lateinit var passwordDtoToPasswordMapper: PasswordDtoToPasswordMapper
    private lateinit var pagingPasswordUseCase: PagingPasswordUseCase
    private lateinit var addPasswordUseCase: AddPasswordUseCase
    private lateinit var updatePasswordUseCase: UpdatePasswordUseCase
    private lateinit var deletePasswordUseCase: DeletePasswordUseCase
    private lateinit var getPasswordBySiteNameUseCase: GetPasswordBySiteNameUseCase
    private lateinit var passwordGeneratorUseCase: PasswordGeneratorUseCase
    private lateinit var appDispatchers: AppDispatchers
    private lateinit var crudViewModel: CrudViewModel

    private val testDispatchers = UnconfinedTestDispatcher()


    @Before
    fun setup() {
        appDispatchers = HolviTestDispatchers(testDispatchers)
        passwordDtoToPasswordMapper = mockk()
        addPasswordUseCase = mockk()
        pagingPasswordUseCase = mockk()
        getPasswordBySiteNameUseCase = mockk()
        updatePasswordUseCase = mockk()
        deletePasswordUseCase = mockk()
        passwordGeneratorUseCase = mockk()
        crudViewModel = CrudViewModel(
            pagingPassword = pagingPasswordUseCase,
            getPasswordBySiteName = getPasswordBySiteNameUseCase,
            addPassword = addPasswordUseCase,
            updatePassword = updatePasswordUseCase,
            passwordGenerator = passwordGeneratorUseCase,
            deletePassword = deletePasswordUseCase,
            appDispatchers = appDispatchers,
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
                awaitItem() shouldBe AddPasswordState.Success

            }
        }
    }

    @Test
    fun `add password fails, due to empty site name`() {
        runTest {
            crudViewModel.passwordAddState.test {
                crudViewModel.add(generatePassword().copy(siteName = ""))
                (awaitItem() as AddPasswordState.Failure).message shouldBeEqual "You must fill required fields."
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
                awaitItem() shouldBe AddPasswordState.Success
            }
        }
    }

    @Test
    fun `update password fails`() {
        runTest {
            crudViewModel.passwordAddState.test {
                crudViewModel.update(generatePassword().copy(siteName = ""))
                (awaitItem() as AddPasswordState.Failure).message shouldBeEqual "You must fill required fields."
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
                awaitItem() shouldBe DeletePasswordState.Success
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
                awaitItem() shouldBe DeletePasswordState.NotFound

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
                awaitItem() shouldBe DeletePasswordState.Failure
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
                awaitItem() shouldBe DeletePasswordState.Failure
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
                awaitItem() shouldBe DeletePasswordState.Success
                crudViewModel.undoDelete()
                awaitItem() shouldBe DeletePasswordState.Undo
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
                awaitItem() shouldBe DeletePasswordState.Success
                testDispatchers.scheduler.advanceTimeBy(4001)
                crudViewModel.undoDelete()
                awaitItem() shouldBe DeletePasswordState.Failure
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
                awaitItem().length shouldBe 0

                crudViewModel.generate()
                verify(exactly = 1) {
                    passwordGeneratorUseCase(any(), any(), any(), any(), any(), any())
                }
                awaitItem().length shouldBeExactly 8
            }
        }
    }

    @Test
    fun `updates query successfully`() {
        runTest {
            crudViewModel.updateQuery("aa")
            crudViewModel.queryFlow.test {
                awaitItem() shouldBeEqual "aa"
            }
        }
    }


    @Test
    fun `paging passwords throws exception`() {
        runTest {
            every { pagingPasswordUseCase.invoke("") }.throws(Exception())
            crudViewModel.paging.test {
                awaitError()
            }
        }
    }
}
