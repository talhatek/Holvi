package com.tek.holvi.domain

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.tek.holvi.model.Key


class StoreRegistrationKeyUseCase(private val dataStore: DataStore<Preferences>) {
    suspend operator fun invoke(key: Key) {
        Log.e("db issue", "store " + key.id)
        dataStore.edit {
            it[stringPreferencesKey("sq")] = key.id
        }
    }
}