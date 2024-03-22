package com.tek.password.domain

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test


class PasswordGeneratorUseCaseTest {

    private val passwordGeneratorUseCase = PasswordGeneratorUseCase()

    @Test
    fun `generates password - returns expected length`() {
        val expectedLength = 8

        val password = passwordGeneratorUseCase(length = expectedLength)

        assertThat(password.length).isEqualTo(expectedLength)
    }

    @Test
    fun `generates password with all types - contains all types`() {

        val password = passwordGeneratorUseCase(
            isWithLowercase = true,
            isWithUppercase = true,
            isWithNumbers = true,
            isWithSpecial = true,
            length = 4
        )

        val lowercase = password.any { it.isLowerCase() }
        val uppercase = password.any { it.isUpperCase() }
        val number = password.any { it.isDigit() }
        val symbol = password.any { !it.isLetterOrDigit() }

        assertThat(lowercase).isTrue()
        assertThat(uppercase).isTrue()
        assertThat(number).isTrue()
        assertThat(symbol).isTrue()
    }

    @Test
    fun `generates password with out lowercase type - returns with out lowercase`() {

        val password = passwordGeneratorUseCase(
            isWithLowercase = false,
            isWithUppercase = true,
            isWithNumbers = true,
            isWithSpecial = true,
            length = 6
        )

        val lowercase = password.any { it.isLowerCase() }
        val uppercase = password.any { it.isUpperCase() }
        val number = password.any { it.isDigit() }
        val symbol = password.any { !it.isLetterOrDigit() }

        assertThat(lowercase).isFalse()
        assertThat(uppercase).isTrue()
        assertThat(number).isTrue()
        assertThat(symbol).isTrue()
    }

    @Test
    fun `generates password with out uppercase type - returns with out uppercase`() {

        val password = passwordGeneratorUseCase(
            isWithLowercase = true,
            isWithUppercase = false,
            isWithNumbers = true,
            isWithSpecial = true,
            length = 6
        )

        val lowercase = password.any { it.isLowerCase() }
        val uppercase = password.any { it.isUpperCase() }
        val number = password.any { it.isDigit() }
        val symbol = password.any { !it.isLetterOrDigit() }

        assertThat(lowercase).isTrue()
        assertThat(uppercase).isFalse()
        assertThat(number).isTrue()
        assertThat(symbol).isTrue()
    }

    @Test
    fun `generates password with out number type - returns with out number`() {

        val password = passwordGeneratorUseCase(
            isWithLowercase = true,
            isWithUppercase = true,
            isWithNumbers = false,
            isWithSpecial = true,
            length = 6
        )

        val lowercase = password.any { it.isLowerCase() }
        val uppercase = password.any { it.isUpperCase() }
        val number = password.any { it.isDigit() }
        val symbol = password.any { !it.isLetterOrDigit() }

        assertThat(lowercase).isTrue()
        assertThat(uppercase).isTrue()
        assertThat(number).isFalse()
        assertThat(symbol).isTrue()
    }

    @Test
    fun `generates password with out symbol type - returns with out symbol`() {

        val password = passwordGeneratorUseCase(
            isWithLowercase = true,
            isWithUppercase = true,
            isWithNumbers = true,
            isWithSpecial = false,
            length = 6
        )

        val lowercase = password.any { it.isLowerCase() }
        val uppercase = password.any { it.isUpperCase() }
        val number = password.any { it.isDigit() }
        val symbol = password.any { !it.isLetterOrDigit() }

        assertThat(lowercase).isTrue()
        assertThat(uppercase).isTrue()
        assertThat(number).isTrue()
        assertThat(symbol).isFalse()
    }


    @Test
    fun `generates password with forbidden letters - excludes forbidden characters`() {
        val forbiddenLetters =
            ('a'..'y').toList().toCharArray() + ('A'..'Y').toList().toCharArray() + (0..8).toList()
                .toString().toCharArray() + "@#=+!£$%&/*-".toCharArray()


        val password = passwordGeneratorUseCase(
            length = 4,
            forbiddenChars = forbiddenLetters
        )

        println(password)
        assertThat(
            password.toCharArray()
                .any { forbiddenLetters.contains(it) }).isFalse()
        assertThat(password.toCharArray().contains('z')).isTrue()
        assertThat(password.toCharArray().contains('Z')).isTrue()
        assertThat(password.toCharArray().contains('9')).isTrue()
        assertThat(password.toCharArray().contains('?')).isTrue()
        assertThat(password.length).isEqualTo(4)
    }

    @Test
    fun `generates password with zero length - returns empty string`() {
        assertThat(passwordGeneratorUseCase(length = 0)).isEmpty()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `generates password with disabled types - returns empty password`() {
        passwordGeneratorUseCase(
            isWithLowercase = false,
            isWithUppercase = false,
            isWithNumbers = false,
            isWithSpecial = false,
            length = 8
        )
    }
}