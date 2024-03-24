package com.tek.database.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tek.database.dao.PasswordDao

class PasswordPagingSource(
    private val query: String,
    private val passwordDao: PasswordDao
) : PagingSource<Int, PasswordDto>() {
    override fun getRefreshKey(state: PagingState<Int, PasswordDto>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PasswordDto> {
        return try {
            passwordDao.pagingSourcePasswords("%$query%").load(params)
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}


