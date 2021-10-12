package com.example.proyectofinal.ui.offline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyectofinal.R
import com.example.proyectofinal.ui.online.LoginActivity
import kotlinx.android.synthetic.main.activity_offline_main.*
import kotlinx.android.synthetic.main.activity_selector.*

class SelectorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selector)
        boffline.setOnClickListener{
            startActivity(Intent(this@SelectorActivity,OfflineMainActivity::class.java))
        }
        bonline.setOnClickListener{
            startActivity(Intent(this@SelectorActivity,LoginActivity::class.java))
        }

    }
}