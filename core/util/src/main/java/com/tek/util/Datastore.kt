package com.tek.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.tek.util.Constant.getDataStoreName

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = getDataStoreName())
