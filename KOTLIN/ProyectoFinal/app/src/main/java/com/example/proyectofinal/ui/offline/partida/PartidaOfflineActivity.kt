package com.example.proyectofinal.ui.offline.partida

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.proyectofinal.R
import com.example.proyectofinal.ui.offline.OfflineMainActivity
import com.example.proyectofinal.utils.PartidaUtil
import kotlinx.android.synthetic.main.activity_partida_offline.*
import kotlinx.android.synthetic.main.alertofflineresult.*

class PartidaOfflineActivity : AppCompatActivity() {
    lateinit var casillas:Array<ImageView>
    var respaldo = IntArray(9){0}
    var  campeon:String = ""
    var nivel:Int = 0
    lateinit var preferences:SharedPreferences
    lateinit var editor:SharedPreferences.Editor
    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partida_offline)
        preferences = this?.getPreferences(Context.MODE_PRIVATE)
        editor = preferences.edit()
        val extras = intent.extras
        nivel = extras?.getInt("NIVEL")!!
        initCasillas()
        babandonar.setOnClickListener{
            startActivity(Intent(this@PartidaOfflineActivity,OfflineMainActivity::class.java))
        }
        when(nivel){
            1-> title="MODO FACIL"
            2-> title="MODO DIFICIL"
            3-> title="MODO IMPOSIBLE"
        }
    }
    private fun initCasillas() {
        casillas= arrayOf(imageView0,imageView1,imageView2,imageView3,
            imageView4,imageView5,imageView6,imageView7,imageView8)
        casillas.forEach { x-> x.visibility=View.VISIBLE }
        casillas.forEach { x -> x.setImageResource(R.drawable.casilla) }
    }
    fun casillaPulsada(view: View){
        if (campeon.isEmpty()){
            if(respaldo[Integer.parseInt(view.tag.toString())] == 0){
                respaldo[Integer.parseInt(view.tag.toString())] = 1
                actualizarTablero()
                if (respaldo.filter { x -> x!=0 }.count() < 9 && campeon.isEmpty()){
                    when(nivel){
                        1 -> respaldo = PartidaUtil(respaldo).selectorAleatorio()
                        2 -> respaldo = PartidaUtil(respaldo).selectorInteligente()!!
                        3 -> respaldo = PartidaUtil(respaldo).selectorImposible()!!
                    }
                    var handler2 = Handler().postDelayed({
                        actualizarTablero()
                    }, 500)

                }
            }
            else{
                Toast.makeText(this,"Casilla ocupada",Toast.LENGTH_LONG).show()
            }
        }

    }
    fun actualizarTablero(){
        for(i in 0..8){
            when(respaldo[i]){
                0-> casillas[i].setImageResource(R.drawable.casilla)
                1-> casillas[i].setImageResource(R.drawable.crossbone)
                2-> casillas[i].setImageResource(R.drawable.fantasma)
            }
        }
        when(PartidaUtil(respaldo).determinarGanador()){
            1-> campeon = "HUMAN"
            2-> campeon = "MACHINE"
            3-> campeon = "EMPATE"
        }
        if (campeon.isNotEmpty()){
            guardarDatos()

            var handler3 = Handler().postDelayed({
                mostrarDialogo()
            }, 1000)

        }

    }

    @SuppressLint("ResourceType")
    private fun guardarDatos() {
        when(campeon){
            "HUMAN"->{
                when(nivel){
                    1->{
                        var aux = preferences.getInt("victorias1",0)
                        editor.putInt("victorias1",aux+1)
                        editor.commit()
                    }
                    2->{
                        var aux = preferences.getInt("victorias2",0)
                        editor.putInt("victorias2",aux+1)
                        editor.commit()
                    }
                    3->{
                        var aux = preferences.getInt("victorias3",0)
                        editor.putInt("victorias3",aux+1)
                        editor.commit()
                    }
                }
            }
            "MACHINE"->{
                when(nivel){
                    1->{
                        var aux = preferences.getInt("derrotas1",0)
                        editor.putInt("derrotas1",aux+1)
                        editor.commit()
                    }
                    2->{
                        var aux = preferences.getInt("derrotas2",0)
                        editor.putInt("derrotas2",aux+1)
                        editor.commit()
                    }
                    3->{
                        var aux = preferences.getInt("derrotas3",0)
                        editor.putInt("derrotas3",aux+1)
                        editor.commit()
                    }
                }
            }
            "EMPATE"->{
                when(nivel){
                    1->{
                        var aux = preferences.getInt("empates1",0)
                        editor.putInt("empates1",aux+1)
                        editor.commit()
                    }
                    2->{
                        var aux = preferences.getInt("empates2",0)
                        editor.putInt("empates2",aux+1)
                        editor.commit()
                    }
                    3->{
                        var aux = preferences.getInt("empates3",0)
                        editor.putInt("empates3",aux+1)
                        editor.commit()
                    }
                }
            }
        }
    }

    @SuppressLint("ResourceType")
    fun mostrarDialogo(){
        var dialog = Dialog(this@PartidaOfflineActivity)
        dialog.setContentView(R.layout.alertofflineresult)
        dialog.setCancelable(false)
        dialog.show()
        when(campeon){
            "HUMAN"-> dialog.result.text = "HAS GANADO !!!!!!"
            "MACHINE"-> dialog.result.text ="HAS PERDIDO :("
            "EMPATE"-> dialog.result.text = "EMPATE"
        }
        when(nivel){
            1->{
                dialog.showVictoriasP.text = preferences.getInt("victorias1",0).toString()
                dialog.showDerrotasaP.text = preferences.getInt("derrotas1",0).toString()
                dialog.showEmpatesP.text = preferences.getInt("empates1",0).toString()
            }
            2->{
                dialog.showVictoriasP.text = preferences.getInt("victorias2",0).toString()
                dialog.showDerrotasaP.text = preferences.getInt("derrotas2",0).toString()
                dialog.showEmpatesP.text = preferences.getInt("empates2",0).toString()
            }
            3->{
                dialog.showVictoriasP.text = preferences.getInt("victorias3",0).toString()
                dialog.showDerrotasaP.text = preferences.getInt("derrotas3",0).toString()
                dialog.showEmpatesP.text = preferences.getInt("empates3",0).toString()
            }
        }
        dialog.cerrar.setOnClickListener{
            startActivity(Intent(this@PartidaOfflineActivity,OfflineMainActivity::class.java))
            dialog.dismiss()
            finish()
        }
    }
}