package com.tek.holvi.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.tek.holvi.model.Key


class StoreRegistrationKeyUseCase(private val dataStore: DataStore<Preferences>) {
    suspend operator fun invoke(key: Key) {
        dataStore.edit {
            it[stringPreferencesKey("sq")] = key.id
        }
    }
}