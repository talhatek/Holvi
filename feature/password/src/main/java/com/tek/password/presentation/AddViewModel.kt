package com.tek.password.presentation

import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tek.database.dao.PasswordDao
import com.tek.database.model.Password
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AddViewModel(private val passwordDao: PasswordDao) : ViewModel() {
    private val _clearInputsSharedFlow = MutableSharedFlow<ClearFocus>()
    val clearInputsSharedFlow = _clearInputsSharedFlow.asSharedFlow()
    private val password = MutableStateFlow("")
    val passwordStateFlow = password.asStateFlow()
    private val _passwordAddState = MutableSharedFlow<AddPasswordState>()
    val passwordAddState = _passwordAddState.asSharedFlow()
    private val passwordManager = PasswordManager()
    fun addPassword(password: Password) {
        viewModelScope.launch {
            val controlPassword = controlPassword(password = password)
            if (controlPassword) {
                try {
                    passwordDao.addPassword(password = password)
                    _passwordAddState.emit(AddPasswordState.Success)
                    _clearInputsSharedFlow.emit(ClearFocus.Clear)
                } catch (ex: Exception) {
                    _passwordAddState.emit(AddPasswordState.Failure("Password could not added. ${ex.message}"))
                }
            } else
                _passwordAddState.emit(AddPasswordState.Failure("You must fill required fields."))
        }
    }

    private fun controlPassword(password: Password): Boolean {
        return listOf(
            password.password,
            password.siteName,
            password.userName
        ).all { it.isNotBlank() and it.isNotEmpty() }
    }

    fun generatePassword(): String {
        val data = passwordManager.generatePassword(length = 8)
        viewModelScope.launch {
            password.emit(data)
        }
        return data
    }

}

sealed interface ClearFocus {
    data object Init : ClearFocus
    data object Clear : ClearFocus
}

sealed class AddPasswordState {
    data object Success : AddPasswordState()
    class Failure(val message: String) : AddPasswordState()
    data object Empty : AddPasswordState()
}


class PasswordManager {

    private var lowerCaseLetters: String = "abcdefghijklmnopqrstuvwxyz"
    private var uppercaseLetters: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private var numbers: String = "0123456789"
    private var symbol: String = "@#=+!£$%&?/*-"

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

        fun String.encrypt(password: String): String {
            val secretKeySpec = SecretKeySpec(password.repeat(4).toByteArray(), "AES")
            val iv = ByteArray(16)
            val charArray = password.toCharArray()
            for (i in charArray.indices) {
                iv[i] = charArray[i].code.toByte()
            }
            val ivParameterSpec = IvParameterSpec(iv)

            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)

            val encryptedValue = cipher.doFinal(this.toByteArray())
            return Base64.encodeToString(encryptedValue, Base64.DEFAULT)
        }

        fun String.decrypt(password: String): String {
            val secretKeySpec = SecretKeySpec(password.repeat(4).toByteArray(), "AES")
            val iv = ByteArray(16)
            val charArray = password.toCharArray()
            for (i in charArray.indices) {
                iv[i] = charArray[i].code.toByte()
            }
            val ivParameterSpec = IvParameterSpec(iv)

            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)

            val decryptedByteValue = cipher.doFinal(Base64.decode(this, Base64.DEFAULT))
            return String(decryptedByteValue)
        }
    }
}