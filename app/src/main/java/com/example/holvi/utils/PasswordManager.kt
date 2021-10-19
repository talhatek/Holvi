package com.example.holvi.utils

import java.security.SecureRandom

class PasswordManager {

    private val lowerCaseLetters: String = "abcdefghijklmnopqrstuvwxyz"
    private val uppercaseLetters: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val numbers: String = "0123456789"
    private val symbol: String = "@#=+!£$%&?/*-"
    private val maxPasswordLength: Float = 20F
    private val maxPasswordFactor: Float = 10F


    // TODO: 19.10.2021 ADD FORBIDDEN LETTERS
    fun generatePassword(
        isWithLetters: Boolean = true,
        isWithUppercase: Boolean = true,
        isWithNumbers: Boolean = true,
        isWithSpecial: Boolean = true,
        length: Int
    ): String {

        var result = ""
        var i = 0

        var lowerCaseCounter = 0
        var upperCaseCounter = 0
        var numberCounter = 0
        var symbolCounter = 0
        var selectedTypeCounter = 0

        if (isWithLetters) {
            result += this.lowerCaseLetters
            selectedTypeCounter++
        }
        if (isWithUppercase) {
            result += this.uppercaseLetters
            selectedTypeCounter++

        }
        if (isWithNumbers) {
            result += this.numbers
            selectedTypeCounter++

        }
        if (isWithSpecial) {
            result += this.symbol
            selectedTypeCounter++
        }

        val rnd = SecureRandom.getInstance("SHA1PRNG")
        val sb = StringBuilder(length)

        while (i < length) {
            val randomInt: Int = rnd.nextInt(result.length)
            if (lowerCaseLetters.contains(result[randomInt]))
                if (lowerCaseCounter < (length / selectedTypeCounter)) {
                    lowerCaseCounter++
                    sb.append(result[randomInt])
                    i++
                } else {
                    if (lowerCaseCounter == (length / selectedTypeCounter)) {
                        result = result.filterNot { lowerCaseLetters.indexOf(it) > -1 }
                    }
                    continue
                }
            else if (uppercaseLetters.contains(result[randomInt]))
                if (upperCaseCounter < (length / selectedTypeCounter)) {
                    upperCaseCounter++
                    sb.append(result[randomInt])
                    i++
                } else {
                    if (upperCaseCounter == (length / selectedTypeCounter)) {
                        result = result.filterNot { uppercaseLetters.indexOf(it) > -1 }
                    }
                    continue
                }
            else if (numbers.contains(result[randomInt]))
                if (numberCounter < (length / selectedTypeCounter)) {
                    numberCounter++
                    sb.append(result[randomInt])
                    i++
                } else {
                    if (numberCounter == (length / selectedTypeCounter)) {
                        result = result.filterNot { numbers.indexOf(it) > -1 }
                    }
                    continue
                }
            else if (symbol.contains(result[randomInt]))
                if (symbolCounter < (length / selectedTypeCounter)) {
                    symbolCounter++
                    sb.append(result[randomInt])
                    i++
                } else {
                    if (symbolCounter == (length / selectedTypeCounter)) {
                        result = result.filterNot { symbol.indexOf(it) > -1 }

                    }
                    continue
                }
        }
        return sb.toString()
    }

    /**
     * Evaluate a random password
     * @param passwordToTest String with the password to test
     * @return a number from 0 to 1, 0 is a very bad password and 1 is a perfect password
     */
    fun evaluatePassword(passwordToTest: String): Float {

        var factor = 0
        val length = passwordToTest.length

        if (passwordToTest.matches(Regex(".*[" + this.lowerCaseLetters + "].*"))) {
            factor += 2
        }
        if (passwordToTest.matches(Regex(".*[" + this.uppercaseLetters + "].*"))) {
            factor += 2
        }
        if (passwordToTest.matches(Regex(".*[" + this.numbers + "].*"))) {
            factor += 1
        }
        if (passwordToTest.matches(Regex(".*[" + this.symbol + "].*"))) {
            factor += 5
        }

        return (factor * length) / (maxPasswordFactor * maxPasswordLength)
    }

}