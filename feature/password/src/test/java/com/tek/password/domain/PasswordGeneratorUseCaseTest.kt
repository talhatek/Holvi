package com.tek.password.domain

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.string.shouldBeEmpty
import kotlin.test.Test


class PasswordGeneratorUseCaseTest {

    private val passwordGeneratorUseCase = PasswordGeneratorUseCase()

    @Test
    fun `generates password - returns expected length`() {
        val expectedLength = 8

        val password = passwordGeneratorUseCase(length = expectedLength)

        password.length shouldBeExactly expectedLength

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


        lowercase.shouldBeTrue()
        uppercase.shouldBeTrue()
        number.shouldBeTrue()
        symbol.shouldBeTrue()
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

        lowercase.shouldBeFalse()
        uppercase.shouldBeTrue()
        number.shouldBeTrue()
        symbol.shouldBeTrue()
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

        lowercase.shouldBeTrue()
        uppercase.shouldBeFalse()
        number.shouldBeTrue()
        symbol.shouldBeTrue()
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

        lowercase.shouldBeTrue()
        uppercase.shouldBeTrue()
        number.shouldBeFalse()
        symbol.shouldBeTrue()
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

        lowercase.shouldBeTrue()
        uppercase.shouldBeTrue()
        number.shouldBeTrue()
        symbol.shouldBeFalse()
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

        password.toCharArray()
            .any { forbiddenLetters.contains(it) }.shouldBeFalse()
        password.toCharArray().contains('z').shouldBeTrue()
        password.toCharArray().contains('Z').shouldBeTrue()
        password.toCharArray().contains('9').shouldBeTrue()
        password.toCharArray().contains('?').shouldBeTrue()
        password.length shouldBeExactly 4
    }

    @Test
    fun `generates password with zero length - returns empty string`() {
        passwordGeneratorUseCase.invoke(length = 0).shouldBeEmpty()
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