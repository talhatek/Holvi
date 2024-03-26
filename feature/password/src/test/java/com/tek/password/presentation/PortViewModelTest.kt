package com.tek.password.presentation

import app.cash.turbine.test
import com.tek.database.domain.AddEncryptedPasswordUseCase
import com.tek.database.domain.ExportPasswordUseCase
import com.tek.database.domain.GetAllPasswordsUseCase
import com.tek.database.domain.ImportPasswordUseCase
import com.tek.database.model.Password
import com.tek.test.HolviTestCipherProvider
import com.tek.test.HolviTestDispatchers
import com.tek.util.AppDispatchers
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PortViewModelTest {

    private lateinit var portViewModel: PortViewModel
    private lateinit var getAllPasswordsUseCase: GetAllPasswordsUseCase
    private lateinit var addPasswordUseCase: AddEncryptedPasswordUseCase
    private lateinit var importPasswordUseCase: ImportPasswordUseCase
    private lateinit var exportPasswordUseCase: ExportPasswordUseCase

    private lateinit var appDispatchers: AppDispatchers
    private lateinit var cipherProvider: HolviTestCipherProvider


    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatchers = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        appDispatchers = HolviTestDispatchers(testDispatchers)
        cipherProvider = HolviTestCipherProvider()
        addPasswordUseCase = AddEncryptedPasswordUseCase(mockk(), cipherProvider)
        importPasswordUseCase = ImportPasswordUseCase(mockk(), cipherProvider)
        exportPasswordUseCase = mockk()
        getAllPasswordsUseCase = mockk()
        portViewModel = PortViewModel(
            getAllPasswords = getAllPasswordsUseCase,
            addEncryptedPassword = addPasswordUseCase,
            importPassword = importPasswordUseCase,
            exportPassword = exportPasswordUseCase,
            appDispatchers = appDispatchers
        )
    }

    private fun generatePassword(id: Int = 0) = Password(
        id = id,
        siteName = "Site Name",
        password = "Password",
        userName = "User Name"
    )

    @Test
    fun `import passwords successfully`() {
        runTest {
            coEvery { getAllPasswordsUseCase.invoke() } returns listOf(
                generatePassword(0),
            )
            coEvery { importPasswordUseCase("1111", "0", generatePassword(0)) } just runs
            portViewModel.portResult.test {
                portViewModel.onEvent(PortEvent.Import)

            }
        }
    }
}