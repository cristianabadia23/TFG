package com.example.proyectofinal.providers

import com.example.proyectofinal.models.Message
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class MessageProvider {
    var mCollection: CollectionReference? = null

    init {
        mCollection = FirebaseFirestore.getInstance().collection("Messages")
    }

    fun create(message: Message): Task<Void?>? {
        val document = mCollection!!.document()
        message.id = document.id
        return document.set(message)
    }
    fun getMessageByChat(idChat: String?): Query? {
        return mCollection!!.whereEqualTo("idChat", idChat).orderBy("timestamp", Query.Direction.ASCENDING)
    }
    fun getMessagesByChatAndSender(idChat: String?, idSender: String?): Query? {
        return mCollection!!.whereEqualTo("idChat", idChat).whereEqualTo("idSender", idSender).whereEqualTo("viewed", false)
    }
}