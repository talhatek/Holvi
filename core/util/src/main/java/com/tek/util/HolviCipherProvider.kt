package com.tek.util

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class HolviCipherProvider : CipherProvider {

    override fun decrypt(input: String, key: String): String {
        val secretKeySpec = SecretKeySpec(key.repeat(4).toByteArray(), "AES")
        val iv = ByteArray(16)
        val charArray = key.toCharArray()
        for (i in charArray.indices) {
            iv[i] = charArray[i].code.toByte()
        }
        val ivParameterSpec = IvParameterSpec(iv)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)

        val decryptedByteValue = cipher.doFinal(Base64.decode(input, Base64.DEFAULT))
        return String(decryptedByteValue)
    }

    override fun encrypt(input: String, key: String): String {
        val secretKeySpec = SecretKeySpec(key.repeat(4).toByteArray(), "AES")
        val iv = ByteArray(16)
        val charArray = key.toCharArray()
        for (i in charArray.indices) {
            iv[i] = charArray[i].code.toByte()
        }
        val ivParameterSpec = IvParameterSpec(iv)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)

        val encryptedValue = cipher.doFinal(input.toByteArray())
        return Base64.encodeToString(encryptedValue, Base64.DEFAULT)
    }
}