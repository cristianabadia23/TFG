package com.example.proyectofinal.ui.online.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.proyectofinal.R
import com.example.proyectofinal.models.Partida
import com.example.proyectofinal.providers.AuthProvider
import com.example.proyectofinal.providers.PartidaProvider
import com.example.proyectofinal.ui.online.partidaonline.CrearPartidaActivity
import com.example.proyectofinal.ui.online.partidaonline.OponenteAleatorioActivity
import com.example.proyectofinal.ui.online.partidaonline.PartidaOnlineActivity
import com.example.proyectofinal.ui.online.partidaonline.ScannerActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.fragment_game_blank.*


class GameBlankFragment : Fragment() {
    var partidaProvider:PartidaProvider = PartidaProvider()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game_blank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ibuscar.setOnClickListener{
           buscarPartida()
        }
        bcrearPartida.setOnClickListener{
            startActivity(Intent(context,CrearPartidaActivity::class.java))
        }
        bescanear.setOnClickListener{
            startActivity(Intent(context,ScannerActivity::class.java))
        }
        boponentealeatorio.setOnClickListener{
            startActivity(Intent(context,OponenteAleatorioActivity::class.java))
        }
    }
    fun buscarPartida(){
        if (textid.text.isNotEmpty()){
            partidaProvider.getPartida(textid.text.toString()).addSnapshotListener{value,error->
                if (value != null) {
                    if (value.exists()){
                        var partida = value.toObject(Partida::class.java)
                        if (partida != null) {
                            if(partida.jugador1.isEmpty() || partida.jugador2.isEmpty()) {
                                if (partida.jugador1.isEmpty()) {
                                    partida.jugador1 = AuthProvider().getUid().toString()
                                    iniciarPartida(value.id, partida)
                                } else if (partida.jugador2.isEmpty()) {
                                    partida.jugador2 = AuthProvider().getUid().toString()
                                    iniciarPartida(value.id, partida)
                                }
                            }
                            else{
                                Toast.makeText(context,"Partida ocupada",Toast.LENGTH_LONG).show()
                            }

                        }
                    }
                    else{
                        Toast.makeText(context,"Problema de conexion",Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    Toast.makeText(context,"PARTIDA NO ENCONTRADA",Toast.LENGTH_LONG).show()
                }
            }
        }
        else{
            Toast.makeText(context,"ID no valido",Toast.LENGTH_LONG).show()
        }
    }
    fun iniciarPartida(partidaId:String,partidaupdate:Partida){
        partidaProvider.updatePartida(partidaId,partidaupdate).addOnSuccessListener {
            var intent = Intent(context, PartidaOnlineActivity::class.java)
            intent.putExtra("PARTIDAID",partidaId)
            var handler2 = Handler().postDelayed({
                startActivity(intent)
            }, 1000)
        }
    }
}