package com.tek.database.domain.mapper

import com.tek.database.data.PasswordDto
import com.tek.database.model.Password

class PasswordDtoToPasswordMapper {

    operator fun invoke(dto: PasswordDto) = with(dto) {
        Password(id = id, siteName = siteName, password = password, userName = userName)
    }

    operator fun invoke(dto: List<PasswordDto>) = with(dto) {
        map {
            Password(
                id = it.id,
                siteName = it.siteName,
                password = it.password,
                userName = it.userName
            )
        }
    }
}