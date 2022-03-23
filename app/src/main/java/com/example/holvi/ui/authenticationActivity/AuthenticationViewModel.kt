package com.example.holvi.ui.authenticationActivity


import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class AuthenticationViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _sqState = MutableStateFlow<SQState>(SQState.Init)
    val sqState = _sqState.asStateFlow()

    init {
        viewModelScope.launch {
            checkKey()
        }
    }

    private suspend fun checkKey() {
        try {
            val id = FirebaseInstallations.getInstance().id.await()
            val key =
                db.collection("")
                    .document(id).get().await()
                    .toObject(Key::class.java)
            if (key == null) initKey(id) else _sqState.emit(SQState.Success(key.id))
        } catch (e: FirebaseFirestoreException) {
            _sqState.emit(SQState.Error(e.message ?: "Unknown error"))
        }
    }

    private suspend fun initKey(id: String) {
        val uuid = UUID.randomUUID().toString()
        val key = Key(id = uuid)
        db.collection("unx").document(id + Build.MANUFACTURER).set(key)
        _sqState.emit(SQState.Success(uuid))
    }
}

data class Key(val id: String = "")

sealed class SQState {
    class Success(val id: String) : SQState()
    class Error(val message: String) : SQState()
    object Init : SQState()
}