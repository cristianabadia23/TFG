package com.example.proyectofinal.ui.online.partidaonline

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.proyectofinal.R
import com.example.proyectofinal.models.Partida
import com.example.proyectofinal.models.User
import com.example.proyectofinal.providers.AuthProvider
import com.example.proyectofinal.providers.UserProvider
import com.example.proyectofinal.ui.online.MainOnlineActivity
import kotlinx.android.synthetic.main.activity_end_game.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class EndGameActivity : AppCompatActivity() {
    lateinit var partida:Partida
    val userProvider:UserProvider = UserProvider()
    val authProvider:AuthProvider = AuthProvider()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_game)
        val extras = intent.extras
        if(extras != null ){
            bmenu.setOnClickListener{
                startActivity(Intent(this@EndGameActivity,MainOnlineActivity::class.java))
            }
            authProvider.getUid()?.let {
                userProvider.read(it).addOnSuccessListener {
                    var user:User = it.toObject(User::class.java)!!
                    Log.e("IDE",extras.getString("RESULTADO").toString())
                    if (user != null){
                        when(extras.getString("RESULTADO").toString()){
                            "GANADOR"->{
                                generarConfetti()
                                user.victorias += 1
                                user.puntos += 3
                                aWinner.visibility = View.VISIBLE
                                alooser.visibility = View.GONE
                                aempata.visibility = View.GONE
                                user.cambiarRango()
                                ovictorias.text = "VICTORIAS  ${user.victorias}\t(+1)"
                                oderrtas.text = "DERROTAS   ${user.derrotas}"
                                oempates.text = "EMPATES    ${user.empates}"
                                opuntos.text = "PUNTOS  ${user.puntos}\t(+3)"
                            }
                            "PERDEDOR"->{
                                user.derrotas += 1
                                user.puntos -= 3
                                aWinner.visibility = View.GONE
                                alooser.visibility = View.VISIBLE
                                aempata.visibility = View.GONE
                                ovictorias.text = "VICTORIAS    ${user.victorias}"
                                oderrtas.text = "DERROTAS   ${user.derrotas}\t(+1)"
                                oempates.text = "EMPATES    ${user.empates}"
                                opuntos.text = "PUNTOS  ${user.puntos}\t(-3)"
                            }
                            "EMPATE"->{
                                user.empates += 1
                                aWinner.visibility = View.GONE
                                alooser.visibility = View.GONE
                                aempata.visibility = View.VISIBLE
                                ovictorias.text = "VICTORIAS    ${user.victorias}"
                                oderrtas.text = "DERROTAS   ${user.derrotas}"
                                oempates.text = "EMPATES    ${user.empates}\t(+1)"
                                opuntos.text = "PUNTOS  ${user.puntos}"
                            }
                        }
                        resultado.text = extras.getString("RESULTADO")
                        user.cambiarRango()
                        onivel.text = "${user.nivel}"
                        onick.text = user.nick.toString()
                        Glide.with(this).load(user.imageURI).into(oimagenperfil)
                        userProvider.create(user.id,user)?.addOnSuccessListener {  }
                    }
                }
            }
        }
    }

    private fun generarConfetti() {
        confetti.build()
            .addColors(Color.BLACK,Color.BLUE,Color.YELLOW)
            .setDirection(0.0,359.0)
            .setSpeed(1f,1f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.Circle,Shape.Square)
            .addSizes(Size(12))
            .setPosition(-50f, confetti.width + 50f, -50f, -50f)
            .streamFor(300, 5000L)
    }

}