package com.tek.util

object Constant {
    private const val DATA_STORE_REGISTRATION_NAME = "registration"
    const val DATA_STORE_REGISTRATION_KEY = "sq"
    const val DATA_STORE_REGISTRATION_DEFAULT_KEY = "default"
    private const val ROOM_DB_NAME = "holvi.db"
    private const val DEBUG_SUFFIX = "debug"

    fun getRoomDbName() = if (BuildConfig.DEBUG) with(ROOM_DB_NAME.split(".")) {
        first() + DEBUG_SUFFIX + "." + last()
    }
    else {
        ROOM_DB_NAME
    }

    fun getDataStoreName() = if (BuildConfig.DEBUG) {
        DATA_STORE_REGISTRATION_NAME + DEBUG_SUFFIX
    } else {
        DATA_STORE_REGISTRATION_NAME
    }
}