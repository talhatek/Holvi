package com.example.holvi.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Password(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "site_name") val siteName: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "user_name") val userName: String

)