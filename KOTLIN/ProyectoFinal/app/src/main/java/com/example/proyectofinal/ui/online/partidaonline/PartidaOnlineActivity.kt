package com.example.proyectofinal.ui.online.partidaonline

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectofinal.R
import com.example.proyectofinal.models.Partida
import com.example.proyectofinal.providers.AuthProvider
import com.example.proyectofinal.providers.PartidaProvider
import com.example.proyectofinal.providers.UserProvider
import com.example.proyectofinal.utils.PartidaUtil
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_partida_online.*

class PartidaOnlineActivity : AppCompatActivity() {
    lateinit var partidaid:String
    lateinit var partida:Partida
    val iCasillas = arrayOf(R.drawable.casilla, R.drawable.casillacolor, R.drawable.locator)
    var iCasilla = 0
    var partidaProvider:PartidaProvider = PartidaProvider()
    var authProvider:AuthProvider = AuthProvider()
    var userProvider:UserProvider = UserProvider()
    lateinit var casillas:Array<ImageView>
    var nJugador1 = ""
    var nJugador2 = ""
    var listener:ListenerRegistration? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partida_online)
        val extras = intent.extras
        if (extras != null) {
            babandonoo.setOnClickListener{
                abandonar()
            }
            partidaid = extras.getString("PARTIDAID").toString()
            Log.e("ID PARTIDA", partidaid)
            initCasillas()
        }
    }

    private fun abandonar() {
        partida.abandonar = authProvider.getUid().toString()
        partida.ganador = if (authProvider.getUid().toString() == partida.jugador1) partida.jugador2
                            else partida.jugador1
        partidaProvider.updatePartida(partidaid,partida).addOnSuccessListener {
            val i = Intent(this@PartidaOnlineActivity, EndGameActivity::class.java)
            i.putExtra("RESULTADO", if (partida.ganador == authProvider.getUid()) "GANADOR" else "PERDEDOR")
            startActivity(i)
            finish()
        }
    }

    private fun initCasillas() {
        casillas= arrayOf(ocasilla0, ocasilla1, ocasilla2, ocasilla3, ocasilla4, ocasilla5, ocasilla6,
                ocasilla7, ocasilla8)
        casillas.forEach { x-> x.visibility=View.VISIBLE }
        casillas.forEach { x -> x.setImageResource(R.drawable.casilla) }
    }

    fun casillaPulsadaOnline(view: View){
        if (partida != null){
            if(partida.ganador.isEmpty()){
                if ((partida.turno && partida.jugador1 == authProvider.getUid()) ||
                        (!partida.turno && partida.jugador2 == authProvider.getUid())){
                    actualizarPartida(view.tag.toString().toInt())
                }
                else{
                    Toast.makeText(this, "NO ES TU TURNO", Toast.LENGTH_LONG).show()
                }
            }
        }
        else{
            if(PartidaUtil(partida.tablero.toIntArray()).determinarGanador() != 0) {
                when (PartidaUtil(partida.tablero.toIntArray()).determinarGanador()) {
                    1 -> partida.ganador = partida.jugador1
                    2 -> partida.ganador = partida.jugador2
                    3 -> partida.ganador = "EMPATE"
                }
                cambiarPantalla()
            }
        }
    }

    private fun cambiarPantalla() {
        var resultado = if (PartidaUtil(partida.tablero.toIntArray()).determinarGanador() == 3) {
            "EMPATE"
        } else {
            if (partida.ganador == authProvider.getUid()) "GANADOR" else "PERDEDOR"
        }
        val i = Intent(this@PartidaOnlineActivity, EndGameActivity::class.java)
        i.putExtra("RESULTADO", resultado)
        startActivity(i)
        finish()
    }

    private fun actualizarPartida(ncasilla: Int) {
        if (partida.tablero[ncasilla] == 0){
            if (partida.turno){
                casillas[ncasilla].setImageResource(R.drawable.crossbone)
                partida.tablero[ncasilla] = 1
            }
            else{
                casillas[ncasilla].setImageResource(R.drawable.fantasma)
                partida.tablero[ncasilla] = 2
            }

            partida.turno = !partida.turno

            if(PartidaUtil(partida.tablero.toIntArray()).determinarGanador() != 0) {
                when (PartidaUtil(partida.tablero.toIntArray()).determinarGanador()) {
                    1 -> partida.ganador = partida.jugador1
                    2 -> partida.ganador = partida.jugador2
                    3 -> partida.ganador = "EMPATE"
                }
                cambiarPantalla()
            }
            actualizarFirebase()
        }
    }

    private fun actualizarFirebase() {
        partida?.let { partidaProvider.updatePartida(partidaid, it).addOnSuccessListener {} }
    }

    override fun onStop() {
        super.onStop()
        listener?.remove()
        if(partida.ganador.isEmpty()){
            partida.abandonar= authProvider.getUid().toString()
            actualizarFirebase()
            val i = Intent(this@PartidaOnlineActivity, EndGameActivity::class.java)
            i.putExtra("RESULTADO", "PERDEDOR")
            finish()
            startActivity(i)
        }

    }

    override fun onStart() {
        super.onStart()
        if (partidaid.isNotEmpty()){
            escucharMovimiento()
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    fun escucharMovimiento(){
        listener = partidaProvider.getPartida(partidaid).addSnapshotListener { value, error ->
            val source = if (value != null && value.metadata.hasPendingWrites()) "Local" else "Server"
            if (value != null) {
                if (value.exists() && source == "Server") {
                    partida = value.toObject(Partida::class.java)!!
                    if (nJugador1.isEmpty() || nJugador2.isEmpty()) {
                        iCasilla = iCasillas[partida.casilla]
                        getPlayerGame()
                    }

                    actualizarTablero()

                }
                actualizarJugadores()
            }
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun actualizarJugadores() {
        if (partida.turno){
            linearjugador1.background = resources.getDrawable(R.drawable.turnoactual)
            linearjugador2.background = resources.getDrawable(R.drawable.turnonoactual)
        }
        if(!partida.turno){
            linearjugador2.background = resources.getDrawable(R.drawable.turnoactual)
            linearjugador1.background = resources.getDrawable(R.drawable.turnonoactual)
        }
        Log.e("Estado",partida.turno.toString())
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun actualizarTablero() {
        if (partida.turno){
            linearjugador1.background = resources.getDrawable(R.drawable.turnoactual)
            linearjugador2.background = resources.getDrawable(R.drawable.turnonoactual)
        }
        if(!partida.turno){
            linearjugador2.background = resources.getDrawable(R.drawable.turnoactual)
            linearjugador1.background = resources.getDrawable(R.drawable.turnonoactual)
        }
        for(i in 0 until partida.tablero.size){
            when(partida.tablero[i]){
                0 -> casillas[i].setImageResource(iCasilla)
                1 -> casillas[i].setImageResource(R.drawable.crossbone)
                2 -> casillas[i].setImageResource(R.drawable.fantasma)
            }

        }
        if (partida.abandonar.isNotEmpty()){
            var resultado = "GANADOR"
            if (partida.abandonar == authProvider.getUid()){
                resultado = "PERDEDOR"
            }
            val i = Intent(this@PartidaOnlineActivity, EndGameActivity::class.java)
            i.putExtra("RESULTADO", resultado)
            finish()
            startActivity(i)
        }
        if (partida.ganador.isNotEmpty()){
            cambiarPantalla()
        }
    }

    private fun getPlayerGame() {
        userProvider.read(partida.jugador1).addOnSuccessListener {
            nJugador1 = it.get("nick").toString()
            jugador1Nombre.text = nJugador1
        }
        userProvider.read(partida.jugador2).addOnSuccessListener {
            nJugador2 = it.get("nick").toString()
            jugador2Nombre.text = nJugador2
        }
    }
}