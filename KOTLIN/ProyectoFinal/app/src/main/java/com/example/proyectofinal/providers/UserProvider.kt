package com.example.proyectofinal.providers

import android.util.Log
import com.example.proyectofinal.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class UserProvider {
    private var mCollection: CollectionReference
    init {
        mCollection = FirebaseFirestore.getInstance().collection("Users")
    }
    fun create(id:String,user: User): Task<Void?>? {
        return mCollection.document(id).set(user)
    }

    fun read(id: String): Task<DocumentSnapshot> {
        return mCollection.document(id).get()
    }
    fun getCollectionReference():CollectionReference{
        return mCollection
    }

}