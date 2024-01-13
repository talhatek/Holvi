package com.example.holvi.di

import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.holvi.db.HolviDb
import com.example.holvi.ui.HolviApp
import com.example.holvi.ui.add_screen.AddViewModel
import com.example.holvi.ui.all_screen.AllViewModel
import com.example.holvi.ui.authenticationActivity.AuthenticationViewModel
import com.example.holvi.ui.generate_screen.GenerateViewModel
import com.example.holvi.ui.port_screen.PortViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.Koin
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module

val roomModule = module {
    single {
        Room.databaseBuilder(androidApplication(), HolviDb::class.java, "holvi.db")
            .openHelperFactory(get()).build()
    }
    single {
        get<HolviDb>().passwordDao
    }

    single<SupportSQLiteOpenHelper.Factory> {
        val passphrase: ByteArray =
            SQLiteDatabase.getBytes((androidApplication() as HolviApp).firebaseIdentifier.toCharArray())
        return@single SupportFactory(passphrase)
    }
}

val viewModule = module {
    viewModelOf(::AddViewModel)
    viewModelOf(::AllViewModel)
    viewModelOf(::AuthenticationViewModel)
    viewModelOf(::PortViewModel)
    viewModelOf(::GenerateViewModel)

}

fun Koin.getViewModelScope(name: String): Scope {
    val scope = getOrCreateScope(name, named(name))
    if (scope.closed) {
        deleteScope(name)
        return getOrCreateScope(name, named(name))
    }
    return scope
}

val dbModule = module {
    factory { Firebase.firestore }
}