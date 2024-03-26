package com.tek.database.domain

import com.tek.database.dao.PasswordDao
import com.tek.database.data.PasswordDto
import com.tek.database.model.Password
import com.tek.util.CipherProvider

class AddEncryptedPasswordUseCase(
    private val passwordDao: PasswordDao,
    private val cipherProvider: CipherProvider
) {

    suspend operator fun invoke(pathId: String, item: Password) {
        passwordDao.addPassword(
            with(item) {
                PasswordDto(
                    id = 0,
                    siteName = cipherProvider.decrypt(siteName, pathId),
                    password = cipherProvider.decrypt(password, pathId),
                    userName = cipherProvider.decrypt(userName, pathId),
                )
            }
        )
    }
}