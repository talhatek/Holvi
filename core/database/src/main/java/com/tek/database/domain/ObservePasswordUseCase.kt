package com.tek.database.domain

import android.util.Log
import com.tek.database.dao.PasswordDao
import com.tek.database.model.Password
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion


class ObservePasswordUseCase(private val passwordDao: PasswordDao) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(query: String) = passwordDao.observeAllPasswords("%${query}%")
        .flatMapConcat { passwords ->
            flowOf(passwords.map {
                Password(
                    id = it.id,
                    siteName = it.siteName,
                    password = it.password,
                    userName = it.userName
                )
            })
        }.onCompletion {

            Log.e("ObservePasswordUseCase", it.toString())
        }
}