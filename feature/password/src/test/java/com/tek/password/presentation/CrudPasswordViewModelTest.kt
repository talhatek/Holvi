package com.tek.password.presentation

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
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
import com.tek.test.TestDiffCallback
import com.tek.test.TestListCallback
import com.tek.util.AppDispatchers
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.ints.shouldBeZero
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CrudPasswordViewModelTest {

    private lateinit var passwordDtoToPasswordMapper: PasswordDtoToPasswordMapper
    private lateinit var pagingPasswordUseCase: PagingPasswordUseCase
    private lateinit var addPasswordUseCase: AddPasswordUseCase
    private lateinit var updatePasswordUseCase: UpdatePasswordUseCase
    private lateinit var deletePasswordUseCase: DeletePasswordUseCase
    private lateinit var getPasswordBySiteNameUseCase: GetPasswordBySiteNameUseCase
    private lateinit var passwordGeneratorUseCase: PasswordGeneratorUseCase
    private lateinit var appDispatchers: AppDispatchers
    private lateinit var crudPasswordViewModel: CrudPasswordViewModel

    private val testDispatchers = UnconfinedTestDispatcher()


    @Before
    fun setup() {
        //we need for .flowOn operations... appDispatchers not working there
        Dispatchers.setMain(testDispatchers)

        appDispatchers = HolviTestDispatchers(testDispatchers)
        passwordDtoToPasswordMapper = mockk()
        addPasswordUseCase = mockk()
        pagingPasswordUseCase = mockk()
        getPasswordBySiteNameUseCase = mockk()
        updatePasswordUseCase = mockk()
        deletePasswordUseCase = mockk()
        passwordGeneratorUseCase = mockk()
        crudPasswordViewModel = CrudPasswordViewModel(
            pagingPassword = pagingPasswordUseCase,
            getPasswordBySiteName = getPasswordBySiteNameUseCase,
            addPassword = addPasswordUseCase,
            updatePassword = updatePasswordUseCase,
            passwordGenerator = passwordGeneratorUseCase,
            deletePassword = deletePasswordUseCase,
            appDispatchers = appDispatchers,
        )

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun generatePassword(id: Int = 0) = Password(
        id = id,
        siteName = "Site Name",
        password = "Password",
        userName = "User Name"
    )

    @Test
    fun `add password successfully`() {
        runTest {
            coEvery { addPasswordUseCase.invoke(generatePassword()) } just runs
            crudPasswordViewModel.passwordAddState.test {
                crudPasswordViewModel.add(generatePassword())
                coVerify(exactly = 1) { addPasswordUseCase.invoke(generatePassword()) }
                awaitItem() shouldBe AddPasswordState.Success

            }
        }
    }

    @Test
    fun `add password fails, due to empty site name`() {
        runTest {
            crudPasswordViewModel.passwordAddState.test {
                crudPasswordViewModel.add(generatePassword().copy(siteName = ""))
                (awaitItem() as AddPasswordState.Failure).message shouldBeEqual "You must fill required fields."
            }
        }
    }

    @Test
    fun `update password successfully`() {
        runTest {
            coEvery { updatePasswordUseCase.invoke(generatePassword()) } just runs
            crudPasswordViewModel.passwordAddState.test {
                crudPasswordViewModel.update(generatePassword())
                coVerify(exactly = 1) { updatePasswordUseCase.invoke(generatePassword()) }
                awaitItem() shouldBe AddPasswordState.Success
            }
        }
    }

    @Test
    fun `update password fails`() {
        runTest {
            crudPasswordViewModel.passwordAddState.test {
                crudPasswordViewModel.update(generatePassword().copy(siteName = ""))
                (awaitItem() as AddPasswordState.Failure).message shouldBeEqual "You must fill required fields."
            }
        }
    }

    @Test
    fun `delete password successfully`() {
        runTest {
            coEvery { getPasswordBySiteNameUseCase.invoke(generatePassword().id) } returns generatePassword()
            coEvery { deletePasswordUseCase.invoke(generatePassword()) } returns 1
            crudPasswordViewModel.passwordDeleteState.test {
                crudPasswordViewModel.delete(generatePassword().id)
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
            crudPasswordViewModel.passwordDeleteState.test {
                crudPasswordViewModel.delete(generatePassword().id)
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
            crudPasswordViewModel.passwordDeleteState.test {
                crudPasswordViewModel.delete(generatePassword().id)
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
            crudPasswordViewModel.passwordDeleteState.test {
                crudPasswordViewModel.delete(generatePassword().id)
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
            crudPasswordViewModel.passwordDeleteState.test {
                crudPasswordViewModel.delete(generatePassword().id)
                coVerify(exactly = 1) { getPasswordBySiteNameUseCase.invoke(generatePassword().id) }
                coVerify(exactly = 1) { deletePasswordUseCase.invoke(generatePassword()) }
                awaitItem() shouldBe DeletePasswordState.Success
                crudPasswordViewModel.undoDelete()
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
            crudPasswordViewModel.passwordDeleteState.test {
                crudPasswordViewModel.delete(generatePassword().id)
                coVerify(exactly = 1) { getPasswordBySiteNameUseCase.invoke(generatePassword().id) }
                coVerify(exactly = 1) { deletePasswordUseCase.invoke(generatePassword()) }
                awaitItem() shouldBe DeletePasswordState.Success
                testDispatchers.scheduler.advanceTimeBy(4001)
                crudPasswordViewModel.undoDelete()
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
            crudPasswordViewModel.passwordStateFlow.test {
                awaitItem().length shouldBe 0

                crudPasswordViewModel.generate()
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
            crudPasswordViewModel.updateQuery("aa")
            crudPasswordViewModel.queryFlow.test {
                awaitItem() shouldBeEqual "aa"
            }
        }
    }

    @Test
    fun `paging passwords success`() {
        runTest {
            val response = PagingData.from(listOf(generatePassword()))
            val test = AsyncPagingDataDiffer(
                diffCallback = TestDiffCallback<Password>(),
                updateCallback = TestListCallback(),
                workerDispatcher = testDispatchers
            )
            every { pagingPasswordUseCase.invoke("") } returns flowOf(response)
            crudPasswordViewModel.paging.test {
                val item = awaitItem()
                test.submitData(item)
                test.itemCount shouldBe 1
            }
        }
    }

    @Test
    fun `paging passwords empty`() {
        runTest {
            val response = PagingData.empty<Password>()
            val test = AsyncPagingDataDiffer(
                diffCallback = TestDiffCallback<Password>(),
                updateCallback = TestListCallback(),
                workerDispatcher = testDispatchers
            )
            every { pagingPasswordUseCase.invoke("") } returns flowOf(response)
            crudPasswordViewModel.paging.test {
                val item = awaitItem()
                test.submitData(item)
                test.itemCount.shouldBeZero()
            }
        }
    }
}
