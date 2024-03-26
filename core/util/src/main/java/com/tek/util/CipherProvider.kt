package com.tek.util

interface CipherProvider {

    fun decrypt(input: String, key: String): String

    fun encrypt(input: String, key: String): String
}