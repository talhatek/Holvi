package com.tek.test

import com.tek.util.CipherProvider

class HolviTestCipherProvider : CipherProvider {
    override fun decrypt(input: String, key: String): String {
        return input
    }

    override fun encrypt(input: String, key: String): String {
        return input
    }
}