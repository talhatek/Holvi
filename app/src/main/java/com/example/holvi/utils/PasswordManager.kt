package com.example.holvi.utils

import java.security.SecureRandom

class PasswordManager {

    private var lowerCaseLetters: String = "abcdefghijklmnopqrstuvwxyz"
    private var uppercaseLetters: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private var numbers: String = "0123456789"
    private var symbol: String = "@#=+!£$%&?/*-"
    private val maxPasswordLength: Float = 20F
    private val maxPasswordFactor: Float = 10F


    fun generatePassword(
        isWithLetters: Boolean = true,
        isWithUppercase: Boolean = true,
        isWithNumbers: Boolean = true,
        isWithSpecial: Boolean = true,
        length: Int,
        forbiddenLetters: CharArray = charArrayOf()
    ): String {

        var result = ""
        var i = 0

        var lowerCaseCounter = 0
        var upperCaseCounter = 0
        var numberCounter = 0
        var symbolCounter = 0
        var selectedTypeCounter = 0

        for (ch in forbiddenLetters) {
            when {
                lowerCaseLetters.contains(ch) -> this.lowerCaseLetters =
                    lowerCaseLetters.replace(ch.toString(), "")
                uppercaseLetters.contains(ch) -> this.uppercaseLetters =
                    uppercaseLetters.replace(ch.toString(), "")
                numbers.contains(ch) -> this.numbers = numbers.replace(ch.toString(), "")
                symbol.contains(ch) -> this.symbol = symbol.replace(ch.toString(), "")
            }
        }

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