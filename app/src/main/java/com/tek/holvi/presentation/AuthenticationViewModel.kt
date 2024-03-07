package com.tek.holvi.presentation


import android.content.SharedPreferences
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.installations.FirebaseInstallations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class AuthenticationViewModel(
    private val db: FirebaseFirestore,
    private val editor: SharedPreferences.Editor
) : ViewModel() {
    private val _sqState = MutableStateFlow<SQState>(SQState.Init)
    val sqState
        get() = _sqState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            checkKey()
        }
    }

    private suspend fun checkKey() {
        try {
            FirebaseInstallations.getInstance().id.await().also { id ->
                db.collection("unx")
                    .document(id + Build.MANUFACTURER).get().await()
                    .toObject(Key::class.java)?.let { key ->
                        editor.putString("sq", key.id).commit()
                        _sqState.emit(SQState.Success(key.id))
                    } ?: run {
                    initKey(id)
                }
            }
        } catch (e: FirebaseFirestoreException) {
            _sqState.emit(SQState.Error(e.message ?: "Unknown error"))
        }
    }

    private suspend fun initKey(id: String) {
        Key(id = UUID.randomUUID().toString()).also { key ->
            db.collection("unx").document(id + Build.MANUFACTURER).set(key)
            _sqState.emit(SQState.Success(key.id))
        }
    }
}

data class Key(val id: String = "")

sealed class SQState {
    class Success(val id: String) : SQState()
    class Error(val message: String) : SQState()
    data object Init : SQState()
}