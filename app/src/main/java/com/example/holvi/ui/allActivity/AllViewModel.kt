package com.example.holvi.ui.allActivity

import androidx.lifecycle.ViewModel
import com.example.holvi.db.dao.PasswordDao
import com.example.holvi.db.model.Password
import kotlinx.coroutines.flow.Flow

class AllViewModel(private val passwordDao: PasswordDao) : ViewModel() {

    fun getAll(): Flow<List<Password>> = passwordDao.getAllPasswords()


}