package com.tek.password.domain

import com.tek.database.model.Password
import java.security.SecureRandom

class PasswordGeneratorUseCase {

    private var lowerCaseLetters: String = "abcdefghijklmnopqrstuvwxyz"
    private var uppercaseLetters: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private var numbers: String = "0123456789"
    private var symbol: String = "@#=+!£$%&?/*-"

    operator fun invoke(
        isWithLowercase: Boolean = true,
        isWithUppercase: Boolean = true,
        isWithNumbers: Boolean = true,
        isWithSpecial: Boolean = true,
        length: Int,
        forbiddenChars: CharArray = charArrayOf()
    ): String {

        var result = ""
        var i = 0

        var lowerCaseCounter = 0
        var upperCaseCounter = 0
        var numberCounter = 0
        var symbolCounter = 0
        var selectedTypeCounter = 0

        for (ch in forbiddenChars) {
            when {
                lowerCaseLetters.contains(ch) -> this.lowerCaseLetters =
                    lowerCaseLetters.replace(ch.toString(), "")

                uppercaseLetters.contains(ch) -> this.uppercaseLetters =
                    uppercaseLetters.replace(ch.toString(), "")

                numbers.contains(ch) -> this.numbers = numbers.replace(ch.toString(), "")
                symbol.contains(ch) -> this.symbol = symbol.replace(ch.toString(), "")
            }
        }

        if (isWithLowercase) {
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
            when {
                lowerCaseLetters.contains(result[randomInt]) -> if (lowerCaseCounter < (length / selectedTypeCounter)) {
                    lowerCaseCounter++
                    sb.append(result[randomInt])
                    i++
                } else {
                    if (lowerCaseCounter == (length / selectedTypeCounter)) {
                        result = result.filterNot { lowerCaseLetters.indexOf(it) > -1 }
                    }
                    continue
                }

                uppercaseLetters.contains(result[randomInt]) -> if (upperCaseCounter < (length / selectedTypeCounter)) {
                    upperCaseCounter++
                    sb.append(result[randomInt])
                    i++
                } else {
                    if (upperCaseCounter == (length / selectedTypeCounter)) {
                        result = result.filterNot { uppercaseLetters.indexOf(it) > -1 }
                    }
                    continue
                }

                numbers.contains(result[randomInt]) -> if (numberCounter < (length / selectedTypeCounter)) {
                    numberCounter++
                    sb.append(result[randomInt])
                    i++
                } else {
                    if (numberCounter == (length / selectedTypeCounter)) {
                        result = result.filterNot { numbers.indexOf(it) > -1 }
                    }
                    continue
                }

                symbol.contains(result[randomInt]) -> if (symbolCounter < (length / selectedTypeCounter)) {
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
        }
        return sb.toString()
    }

    companion object {

        fun Map<String, Any>.toPassword(): Password {
            return Password(
                id = (this.getValue("id") as Long).toInt(),
                password = this.getValue("password") as String,
                userName = this.getValue("userName") as String,
                siteName = this.getValue("siteName") as String,
            )
        }
    }
}