package com.tek.database.domain

import com.tek.database.dao.PasswordDao
import com.tek.database.data.PasswordDto
import com.tek.database.model.Password

class UpdatePasswordUseCase(private val passwordDao: PasswordDao) {

    suspend operator fun invoke(item: Password) {
        passwordDao.updatePassword(
            with(item) {
                PasswordDto(
                    id = item.id,
                    siteName = siteName,
                    password = password,
                    userName = userName
                )
            }
        )
    }
}