package com.tek.database.domain

import com.tek.database.dao.PasswordDao
import com.tek.database.model.Password
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf


class ObservePasswordUseCase(private val passwordDao: PasswordDao) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke() = passwordDao.observeAllPasswords()
        .flatMapConcat { passwords ->
            flowOf(passwords.map { Password(it.siteName, it.password, it.userName) })
        }
}