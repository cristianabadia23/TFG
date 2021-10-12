package com.example.proyectofinal.ui.online.partidaonline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.example.proyectofinal.R
import com.example.proyectofinal.models.Partida
import com.example.proyectofinal.providers.AuthProvider
import com.example.proyectofinal.providers.PartidaProvider
import com.example.proyectofinal.ui.online.MainOnlineActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_crear_partida.*
import kotlinx.android.synthetic.main.activity_oponente_aleatorio.*

class OponenteAleatorioActivity : AppCompatActivity() {
    var partidaProvider: PartidaProvider = PartidaProvider()
    val authProvider = AuthProvider()
    var idUser:String? = authProvider.getUid()
    var partidaId = ""
    var isAcepter = false
    var listener: ListenerRegistration? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oponente_aleatorio)
        progressBar.isIndeterminate = true
        buscarOponente()
        bcancelaraleatorio.setOnClickListener{
            finish()
        }
    }
    private fun buscarOponente() {
        partidaProvider.getAllWhereJugador2Emply().addOnCompleteListener {
            testado.text="BUSCANDO PARTIDA ..."
            if(it.result?.size() == 0){
                var handler2 = Handler().postDelayed({
                    crearPartidaAleatoria()
                }, 1000)
            }
            else{
                testado.text = "PARTIDA ENCONTRADA"
                var resultado = it.getResult()?.documents?.get(0)
                if (resultado != null) {
                    partidaId = resultado.id
                    var partidaEncontrada = resultado.toObject(Partida::class.java)
                    if (partidaEncontrada != null) {
                        partidaEncontrada.jugador2 = idUser.toString()
                        partidaProvider.updatePartida(partidaId,partidaEncontrada)
                            .addOnSuccessListener {
                                if (partidaId.isNotEmpty()){
                                    var intent = Intent(this@OponenteAleatorioActivity,PartidaOnlineActivity::class.java)
                                    intent.putExtra("PARTIDAID",partidaId)
                                    var handler2 = Handler().postDelayed({
                                        isAcepter = true
                                        finish()
                                        startActivity(intent)
                                    }, 1000)
                                }
                            }
                            .addOnFailureListener{
                                Toast.makeText(this,"Error al ingresar en la partida", Toast.LENGTH_LONG).show()
                            }
                    }
                }
            }
        }
    }
    private fun crearPartidaAleatoria(){
        testado.text = "CREANDO PARTIDA ..."
        var partidaRandom: Partida = Partida()
        partidaRandom.jugador1 = idUser.toString()
        partidaProvider.savePartida(partidaRandom).addOnSuccessListener {
            partidaId = it.id
            var handler2 = Handler().postDelayed({
                esperarJugador()
            }, 1000)
        }
    }
    private fun esperarJugador(){
        testado.text = "ESPERANDO OPONENTE  ..."
        listener = partidaProvider.getPartida(partidaId).addSnapshotListener(EventListener<DocumentSnapshot?> { value, error ->
            if (value?.get("jugador2") != "") {
                var intent = Intent(this@OponenteAleatorioActivity,PartidaOnlineActivity::class.java)
                intent.putExtra("PARTIDAID",partidaId)
                var handler2 = Handler().postDelayed({
                    isAcepter = true
                    finish()
                    startActivity(intent)
                    Log.e("CP","CAMBIANDO DE PANTALLA")
                }, 1000)
            }
        })
    }

    override fun onStop() {
        super.onStop()
        listener?.remove()
        if (partidaId.isNotEmpty() && !isAcepter){
            partidaProvider.deletePartida(partidaId).addOnSuccessListener {
            }
        }
    }
}