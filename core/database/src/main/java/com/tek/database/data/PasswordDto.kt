package com.tek.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PasswordDto(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "site_name") val siteName: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "user_name") val userName: String

)