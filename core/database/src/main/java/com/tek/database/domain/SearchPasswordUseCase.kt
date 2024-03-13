package com.tek.database.domain

import com.tek.database.dao.PasswordDao
import com.tek.database.model.Password

class SearchPasswordUseCase(private val passwordDao: PasswordDao) {

    suspend operator fun invoke(query: String) =
        passwordDao.searchThroughPasswords("%$query%").map {
            Password(
                id = it.id,
                siteName = it.siteName,
                password = it.password,
                userName = it.userName
            )
        }

}