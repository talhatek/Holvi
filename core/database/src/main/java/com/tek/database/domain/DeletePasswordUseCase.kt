package com.tek.database.domain

import com.tek.database.dao.PasswordDao
import com.tek.database.model.Password

class DeletePasswordUseCase(private val passwordDao: PasswordDao) {

    suspend operator fun invoke(item: Password) =
        passwordDao.deletePassword(siteName = item.siteName, userName = item.userName)

}