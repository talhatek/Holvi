package com.tek.database.domain

import com.tek.database.dao.PasswordDao
import com.tek.database.domain.mapper.PasswordDtoToPasswordMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf


class ObservePasswordUseCase(
    private val passwordDao: PasswordDao,
    private val mapper: PasswordDtoToPasswordMapper
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(query: String) =
        passwordDao.observeAllPasswords("%${query}%").flatMapLatest { dto ->
            flowOf(mapper.invoke(dto).sortedBy { it.id })
        }

}