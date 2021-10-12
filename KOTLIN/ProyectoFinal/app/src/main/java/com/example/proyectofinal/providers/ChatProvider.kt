package com.example.proyectofinal.providers

import com.example.proyectofinal.models.Chat
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot


class ChatProvider {
    var mCollection: CollectionReference? = null

    init {
        mCollection = FirebaseFirestore.getInstance().collection("Chats")
    }

    fun create(chat: Chat) {
       mCollection!!.document(chat.idUser1 + chat.idUser2).set(chat)

    }

    fun getAll(idUser: String): Query {
        return mCollection!!.whereArrayContains("ids", idUser)
    }

    fun getChatByUser1AndUser2(idUser1: String, idUser2: String): Query? {
        val ids: ArrayList<String> = ArrayList()
        ids.add(idUser1 + idUser2)
        ids.add(idUser2 + idUser1)
        return mCollection!!.whereIn("id", ids)
    }
}