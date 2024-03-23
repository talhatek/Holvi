package com.tek.password.presentation

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.tek.password.domain.PasswordGeneratorUseCase
import com.tek.test.HolviTestDispatchers
import com.tek.util.AppDispatchers
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


fun main() {
    val sequence = generateSequence(4) { prev ->
        if (prev.plus(4) < 50) {
            prev.plus(4)

        } else {
            null
        }
    }
    println(sequence.toList().joinToString { "-$it-" })
}

class GenerateViewModelTest {

    private lateinit var passwordGeneratorUseCase: PasswordGeneratorUseCase
    private lateinit var generateViewModel: GenerateViewModel

    private lateinit var appDispatchers: AppDispatchers

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatchers = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        appDispatchers = HolviTestDispatchers(testDispatchers)
        passwordGeneratorUseCase = mockk()
        generateViewModel = GenerateViewModel(
            passwordGeneratorUseCase,
            appDispatchers
        )
    }

    private fun getDropdownItems(activeCount: Int) =
        generateSequence(activeCount) { prev ->
            if (prev.plus(activeCount) < GenerateViewModel.LENGTH_LIMIT) {
                prev.plus(activeCount)
            } else {
                null
            }
        }.toList()

    @Test
    fun `dropdownItems correct on init `() {
        runTest {
            val activeCount = generateViewModel.activeCount.first()
            generateViewModel.dropdownItems.test {
                assertThat(awaitItem()).isEqualTo(getDropdownItems(activeCount))
            }
        }
    }

    @Test
    fun `dropdownItems correct on lower active count `() {
        runTest {
            generateViewModel.dropdownItems.test {
                awaitItem()
                generateViewModel.updateActiveCount(false)
                val activeCount = generateViewModel.activeCount.first()
                assertThat(awaitItem()).isEqualTo(getDropdownItems(activeCount))

            }
        }
    }

    @Test
    fun `generate password successfully`() {
        every {
            passwordGeneratorUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns "11aaAA!!"
        runTest {
            generateViewModel.currentPassword.test {
                assertThat(awaitItem()).isEmpty()
                generateViewModel.currentSelectedLength.value = 4
                generateViewModel.generatePassword()
                assertThat(awaitItem()).isEqualTo("11aaAA!!")
            }
        }
    }

    @Test
    fun `generate password fail due to non positive currentSelectedLength`() {
        runTest {
            generateViewModel.uiEvent.test {
                generateViewModel.generatePassword()
                assertThat(awaitItem()).isInstanceOf(GenerateViewUiEvent.SnackbarEvent::class.java)

            }
        }
    }

    @Test
    fun `copy  password to clipboard successfully`() {
        runTest {
            generateViewModel.uiEvent.test {
                generateViewModel.currentSelectedLength.value = 4
                generateViewModel.copyToClipBoard(mockk())
                assertThat(awaitItem()).isInstanceOf(GenerateViewUiEvent.SnackbarEvent::class.java)

            }

        }
    }

}