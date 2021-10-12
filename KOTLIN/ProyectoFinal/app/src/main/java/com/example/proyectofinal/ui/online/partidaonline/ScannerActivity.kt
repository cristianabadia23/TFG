package com.example.proyectofinal.ui.online.partidaonline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.proyectofinal.R
import com.example.proyectofinal.models.Partida
import com.example.proyectofinal.providers.AuthProvider
import com.example.proyectofinal.providers.PartidaProvider
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_scanner.*
import kotlinx.android.synthetic.main.fragment_game_blank.*

class ScannerActivity : AppCompatActivity() {
    var partidaProvider:PartidaProvider = PartidaProvider()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        scanner1.visibility = View.VISIBLE
        scanner2.visibility = View.GONE
        scanner3.visibility = View.GONE
        bscanner.setOnClickListener{
            IntentIntegrator(this)
                .initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        if (result != null) {
            buscarPartida(result.contents)
        }
        else {
            tscanner.text = "Codigo no valido"
        }
    }
    fun buscarPartida(pid: String){
        if (pid.isNotEmpty()){
            partidaProvider.getPartida(pid).addSnapshotListener{value,error->
                if (value != null) {
                    if (value.exists()){
                        var partida = value.toObject(Partida::class.java)
                        if (partida != null) {
                            if (partida.jugador1.isEmpty()){
                                partida.jugador1 = AuthProvider().getUid().toString()
                                iniciarPartida(value.id,partida)
                            }
                            else if (partida.jugador2.isEmpty()){
                                partida.jugador2 = AuthProvider().getUid().toString()
                                iniciarPartida(value.id,partida)
                            }
                        }
                        scanner1.visibility = View.GONE
                        scanner2.visibility = View.VISIBLE
                        scanner3.visibility = View.GONE
                    }
                    else{
                        Toast.makeText(this,"PARTIDA NO ENCONTRADA", Toast.LENGTH_LONG).show()
                        scanner1.visibility = View.GONE
                        scanner2.visibility = View.GONE
                        scanner3.visibility = View.VISIBLE

                    }
                }
                else{
                    scanner1.visibility = View.GONE
                    scanner2.visibility = View.GONE
                    scanner3.visibility = View.VISIBLE
                    Toast.makeText(this,"Problema de conexion", Toast.LENGTH_LONG).show()
                }
            }
        }
        else{
            scanner1.visibility = View.GONE
            scanner2.visibility = View.GONE
            scanner3.visibility = View.VISIBLE
            Toast.makeText(this,"ID no valido", Toast.LENGTH_LONG).show()
        }
    }
    fun iniciarPartida(partidaId:String,partidaupdate: Partida){
        partidaProvider.updatePartida(partidaId,partidaupdate).addOnSuccessListener {
            var intent = Intent(this, PartidaOnlineActivity::class.java)
            intent.putExtra("PARTIDAID",partidaId)
            var handler2 = Handler().postDelayed({
                startActivity(intent)
            }, 2000)
        }
    }
}