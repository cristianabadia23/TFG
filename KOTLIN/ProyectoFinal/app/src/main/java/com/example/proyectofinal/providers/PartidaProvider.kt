package com.example.proyectofinal.providers

import com.example.proyectofinal.models.Partida
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*

class PartidaProvider {
    private var mCollection: CollectionReference

    init {
        mCollection = FirebaseFirestore.getInstance().collection("partidas")
    }
    fun savePartida(partida:Partida): Task<DocumentReference> {
        return mCollection.add(partida)
    }
    fun updatePartida(id: String,partida: Partida): Task<Void> {
        return mCollection.document(id).set(partida)
    }
    fun getAllWhereJugador2Emply(): Task<QuerySnapshot> {
        return mCollection.whereEqualTo("jugador2","").whereEqualTo("privated",false).get()
    }
    fun getPartida(id:String): DocumentReference {
        return mCollection.document(id)
    }
    fun deletePartida(id:String): Task<Void> {
        return mCollection.document(id).delete()
    }
}