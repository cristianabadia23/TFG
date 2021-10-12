package com.example.proyectofinal.ui.online.partidaonline

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectofinal.R
import com.example.proyectofinal.models.Partida
import com.example.proyectofinal.providers.AuthProvider
import com.example.proyectofinal.providers.PartidaProvider
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_crear_partida.*
import kotlinx.android.synthetic.main.mostrarinfoalert.*
import net.glxn.qrgen.android.QRCode

class CrearPartidaActivity : AppCompatActivity() {
    var jugador = 1
    var casilla = 1
    var partidaProvider:PartidaProvider = PartidaProvider()
    var authProvider:AuthProvider = AuthProvider()
    val userId = authProvider.getUid()
    var partidaid = ""
    lateinit var dialog:Dialog
    var listener: ListenerRegistration? = null
    @SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_partida)
        dialog = Dialog(this@CrearPartidaActivity)
        sjugador1.setOnClickListener{
            user1.background = resources.getDrawable(R.drawable.turnoactual)
            user2.background = resources.getDrawable(R.drawable.turnonoactual)
            jugador = 1
        }
        sjugador2.setOnClickListener{
            user2.background = resources.getDrawable(R.drawable.turnoactual)
            user1.background = resources.getDrawable(R.drawable.turnonoactual)
            jugador = 2
        }
        scasilla1.setOnClickListener{
            lcasilla1.background = resources.getDrawable(R.drawable.turnoactual)
            lcasilla2.background = resources.getDrawable(R.drawable.turnonoactual)
            lcasilla3.background = resources.getDrawable(R.drawable.turnonoactual)
            casilla = 1
        }
        scasilla2.setOnClickListener{
            lcasilla2.background = resources.getDrawable(R.drawable.turnoactual)
            lcasilla1.background = resources.getDrawable(R.drawable.turnonoactual)
            lcasilla3.background = resources.getDrawable(R.drawable.turnonoactual)
            casilla = 2
        }
        scasilla3.setOnClickListener{
            lcasilla3.background = resources.getDrawable(R.drawable.turnoactual)
            lcasilla2.background = resources.getDrawable(R.drawable.turnonoactual)
            lcasilla1.background = resources.getDrawable(R.drawable.turnonoactual)
            casilla = 3
        }
        crearPartida.setOnClickListener {
            var partida:Partida = Partida()
            when(jugador){
                1 -> partida.jugador1 = userId.toString()
                2 -> partida.jugador2 = userId.toString()
            }
            partida.casilla = casilla-1
            partida.isPrivated = true
            partidaProvider.savePartida(partida)
                .addOnSuccessListener {
                    Toast.makeText(this,"Partida creada",Toast.LENGTH_LONG).show()
                    partidaid = it.id
                    mostrarDialogo()
                }.addOnFailureListener{
                    Toast.makeText(this,"Error al crear la partida",Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.e("Inicio","pantalla iniciada")
    }
    private fun mostrarDialogo() {
        dialog.setContentView(R.layout.mostrarinfoalert)
        dialog.setCancelable(false)
        dialog.show()
        dialog.mostrarQR.setImageBitmap(QRCode.from(partidaid).withSize(100,100).bitmap())
        dialog.mprogressBar.isIndeterminate = true
        dialog.compartir.setOnClickListener {
            val intent = Intent()
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "Para jugar contra mi introduce el siguiente codigo")
            intent.putExtra(Intent.EXTRA_TEXT, partidaid)
            intent.action = Intent.ACTION_SEND
            val chooseIntent = Intent.createChooser(intent, "Elija una opcion")
            startActivity(chooseIntent)
        }
        dialog.cancelarPartida.setOnClickListener{
            partidaProvider.deletePartida(partidaid).addOnSuccessListener {
                dialog.dismiss()
            }
        }
        esperarJugador()
    }

    private fun esperarJugador() {
        listener = partidaProvider.getPartida(partidaid).addSnapshotListener(EventListener<DocumentSnapshot?> { value, error ->
            if (value?.get("jugador2").toString().isNotEmpty() && value?.get("jugador1").toString().isNotEmpty()) {
                val par = value?.toObject(Partida::class.java)
                if (par != null) {
                    iniciarPartida(partidaid,par)
                }
            }

        })
    }
    fun iniciarPartida(partidaId:String,partidaupdate:Partida){
        listener?.remove()
        partidaProvider.updatePartida(partidaId,partidaupdate).addOnSuccessListener {
            var intent = Intent(this@CrearPartidaActivity, PartidaOnlineActivity::class.java)
            intent.putExtra("PARTIDAID",partidaId)
            if (dialog != null){
                dialog.dismiss()
            }
            var handler2 = Handler().postDelayed({
                startActivity(intent)
            }, 1000)
        }
    }
}