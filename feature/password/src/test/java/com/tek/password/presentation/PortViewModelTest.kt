package com.tek.password.presentation

import app.cash.turbine.test
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.tek.database.domain.AddEncryptedPasswordUseCase
import com.tek.database.domain.ExportPasswordUseCase
import com.tek.database.domain.GetAllPasswordsUseCase
import com.tek.database.domain.ImportPasswordUseCase
import com.tek.database.model.Password
import com.tek.test.HolviTestCipherProvider
import com.tek.test.HolviTestDispatchers
import com.tek.util.AppDispatchers
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
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
    private lateinit var fireStore: FirebaseFirestore


    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatchers = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        fireStore = mockk<FirebaseFirestore>(relaxed = false)
        appDispatchers = HolviTestDispatchers(testDispatchers)
        cipherProvider = HolviTestCipherProvider()
        addPasswordUseCase = AddEncryptedPasswordUseCase(mockk(), cipherProvider)
        importPasswordUseCase = ImportPasswordUseCase(fireStore, cipherProvider)
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
            every {
                importPasswordUseCase.invoke("1111", "0", generatePassword(0))
            } returns mockTask()
            portViewModel.portResult.test {
                portViewModel.onEvent(PortEvent.Import(pathId = "1111"))
                awaitItem().shouldBeInstanceOf<PortResult.ImportSuccess>()

            }
        }
    }

    @Test
    fun `import passwords failure`() {
        runTest {
            coEvery { getAllPasswordsUseCase.invoke() } returns listOf(
                generatePassword(0),
            )
            every {
                importPasswordUseCase.invoke("1111", "0", generatePassword(0))
            }.throws(Exception())
            portViewModel.portResult.test {
                portViewModel.onEvent(PortEvent.Import(pathId = "1111"))
                awaitItem().shouldBeInstanceOf<PortResult.Error>()

            }
        }
    }
}

private fun mockTask(): Task<Void> {
    return mockk<Task<Void>>(relaxed = true)
}