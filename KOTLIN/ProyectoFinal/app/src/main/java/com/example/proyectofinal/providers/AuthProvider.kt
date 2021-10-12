package com.example.proyectofinal.providers

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*

class AuthProvider {
    var mAuth: FirebaseAuth
    init {
        mAuth = FirebaseAuth.getInstance()
        mAuth.languageCode = "es"
    }

    fun register(email: String?, password: String?): Task<AuthResult?>? {
        return mAuth!!.createUserWithEmailAndPassword(email, password)
    }

    fun login(email: String?, password: String?): Task<AuthResult?>? {
        return mAuth!!.signInWithEmailAndPassword(email, password)
    }



    fun getEmail(): String? {
        return if (mAuth!!.currentUser != null) {
            mAuth!!.currentUser.email
        } else {
            null
        }
    }

    fun getUid(): String? {
        return if (mAuth!!.currentUser != null) {
            mAuth!!.currentUser.uid
        } else {
            null
        }
    }

    fun getUserSession(): FirebaseUser? {
        return if (mAuth!!.currentUser != null) {
            mAuth!!.currentUser
        } else {
            null
        }
    }

    fun logout() {
        if (mAuth != null) {
            mAuth!!.signOut()
        }
    }
    fun signGoogle(credential: AuthCredential): Task<AuthResult> {
        return mAuth.signInWithCredential(credential)
    }
}