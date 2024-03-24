package com.tek.database.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.tek.database.dao.PasswordDao
import com.tek.database.data.PasswordPagingSource
import com.tek.database.domain.mapper.PasswordDtoToPasswordMapper
import com.tek.database.model.Password
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PagingPasswordUseCase(
    private val passwordDao: PasswordDao,
    private val passwordDtoToPasswordMapper: PasswordDtoToPasswordMapper
) {
    operator fun invoke(
        query: String,
    ): Flow<PagingData<Password>> {
        return Pager(
            config = PagingConfig(pageSize = 25, enablePlaceholders = false),
            pagingSourceFactory = { PasswordPagingSource(query, passwordDao) }
        ).flow.map { it.map(passwordDtoToPasswordMapper::invoke) }
    }
}