package com.example.proyectofinal.ui.offline

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectofinal.R
import com.example.proyectofinal.ui.offline.partida.PartidaOfflineActivity
import kotlinx.android.synthetic.main.activity_offline_main.*

class OfflineMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offline_main)
        tmenu.setOnClickListener{
            startActivity(Intent(this@OfflineMainActivity,SelectorActivity::class.java))
        }
        bfacil.setOnClickListener{
            startActivity(Intent(this@OfflineMainActivity,PartidaOfflineActivity::class.java)
                .putExtra("NIVEL",1))
        }
        bdificil.setOnClickListener{
            startActivity(Intent(this@OfflineMainActivity,PartidaOfflineActivity::class.java)
                .putExtra("NIVEL",2))
        }
        bimposible.setOnClickListener{
            startActivity(Intent(this@OfflineMainActivity,PartidaOfflineActivity::class.java)
                .putExtra("NIVEL",3))
        }
    }
}