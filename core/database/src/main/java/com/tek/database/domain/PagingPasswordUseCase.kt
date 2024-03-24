package com.tek.database.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tek.database.dao.PasswordDao
import com.tek.database.data.PasswordDto
import com.tek.database.data.PasswordPagingSource
import kotlinx.coroutines.flow.Flow

class PagingPasswordUseCase(
    private val passwordDao: PasswordDao
) {
    operator fun invoke(
        query: String,
    ): Flow<PagingData<PasswordDto>> {
        return Pager(
            config = PagingConfig(pageSize = 25, enablePlaceholders = false),
            pagingSourceFactory = { PasswordPagingSource(query, passwordDao) }
        ).flow
    }
}