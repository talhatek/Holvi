package com.tek.holvi.ui.add_screen


import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.tek.database.dao.PasswordDao
import com.tek.database.data.PasswordDto
import com.tek.database.domain.AddPasswordUseCase
import com.tek.database.domain.DeletePasswordUseCase
import com.tek.database.domain.GetPasswordBySiteNameUseCase
import com.tek.database.domain.ObservePasswordUseCase
import com.tek.database.domain.UpdatePasswordUseCase
import com.tek.database.domain.mapper.PasswordDtoToPasswordMapper
import com.tek.database.model.Password
import com.tek.password.domain.PasswordGeneratorUseCase
import com.tek.password.presentation.AddPasswordState
import com.tek.password.presentation.CrudViewModel
import com.tek.password.presentation.PasswordsState
import com.tek.test.HolviTestDispatchers
import com.tek.util.AppDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class CrudViewModelTest {

    @Mock
    private lateinit var passwordDao: PasswordDao

    @Mock
    private lateinit var passwordDtoToPasswordMapper: PasswordDtoToPasswordMapper

    @InjectMocks
    private lateinit var addPasswordUseCase: AddPasswordUseCase

    @InjectMocks
    private lateinit var updatePasswordUseCase: UpdatePasswordUseCase

    @InjectMocks
    private lateinit var deletePasswordUseCase: DeletePasswordUseCase

    @InjectMocks
    private lateinit var getPasswordBySiteNameUseCase: GetPasswordBySiteNameUseCase

    @InjectMocks
    private lateinit var observePasswordUseCase: ObservePasswordUseCase

    @InjectMocks
    private val passwordGeneratorUseCase = PasswordGeneratorUseCase()

    private lateinit var appDispatchers: AppDispatchers

    private lateinit var vm: CrudViewModel


    private val testDispatchers = UnconfinedTestDispatcher()


    @Before
    fun setup() {
        appDispatchers = HolviTestDispatchers(testDispatchers)
        vm = CrudViewModel(
            getPasswordBySiteNameUseCase,
            addPasswordUseCase,
            updatePasswordUseCase,
            passwordGeneratorUseCase,
            deletePasswordUseCase,
            observePasswordUseCase,
            appDispatchers,
            false
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
            Mockito.`when`((observePasswordUseCase.invoke(""))).thenReturn(flowOf(emptyList()))
            vm.passwordsState.test {
                assertThat(awaitItem()).isEqualTo(PasswordsState.Init)
                vm.observePasswords()
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
            Mockito.`when`((observePasswordUseCase.invoke("")))
                .thenReturn(flowOf(listOf(generatePasswordDto())))
            vm.passwordsState.test {
                assertThat(awaitItem()).isEqualTo(PasswordsState.Init)
                vm.observePasswords()
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
