package com.tek.database.domain

import com.tek.database.dao.PasswordDao


class ObservePasswordUseCase(
    private val passwordDao: PasswordDao,
) {

    operator fun invoke(query: String) = passwordDao.observeAllPasswords("%${query}%")

}