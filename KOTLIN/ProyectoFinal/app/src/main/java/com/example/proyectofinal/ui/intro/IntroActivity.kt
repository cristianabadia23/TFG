package com.example.proyectofinal.ui.intro

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyectofinal.R
import com.example.proyectofinal.ui.adapter.IntroAdapter
import com.example.proyectofinal.ui.offline.OfflineMainActivity
import com.example.proyectofinal.ui.offline.SelectorActivity
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

    private lateinit var slideAdapter: IntroAdapter
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        preferences = this?.getPreferences(Context.MODE_PRIVATE)
        editor = preferences.edit()
        if(preferences.getBoolean("introvista",true)){
            editor.putBoolean("introvista",false)
            editor.apply()
            slideAdapter = IntroAdapter(this)
            viewpager.adapter = slideAdapter
        }
        else{
            startActivity(Intent(this@IntroActivity,SelectorActivity::class.java))
            finish()
        }
        bsaltar.setOnClickListener{
            startActivity(Intent(this@IntroActivity,SelectorActivity::class.java))
            finish()
        }
    }
}