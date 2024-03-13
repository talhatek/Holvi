package com.tek.database.domain

import com.tek.database.dao.PasswordDao
import com.tek.database.domain.mapper.PasswordDtoToPasswordMapper

class GetPasswordBySiteNameUseCase(
    private val passwordDao: PasswordDao,
    private val mapper: PasswordDtoToPasswordMapper
) {

    suspend operator fun invoke(siteName: String) =
        mapper.invoke(passwordDao.getPasswordBySiteName(siteName))

}