package com.tek.database.domain

import com.tek.database.dao.PasswordDao
import com.tek.database.data.PasswordDto
import com.tek.database.model.Password

class AddPasswordUseCase(private val passwordDao: PasswordDao) {

    suspend operator fun invoke(item: Password) {
        passwordDao.addPassword(
            with(item) {
                PasswordDto(id = 0, siteName = siteName, password = password, userName = userName)
            }
        )
    }
}