package com.tek.database.domain

import com.tek.database.dao.PasswordDao
import com.tek.database.model.Password

class GetAllPasswordsUseCase(private val passwordDao: PasswordDao) {


    operator fun invoke() = passwordDao.getAllPasswords().map {
        Password(
            id = it.id,
            siteName = it.siteName,
            password = it.password,
            userName = it.userName
        )
    }
}