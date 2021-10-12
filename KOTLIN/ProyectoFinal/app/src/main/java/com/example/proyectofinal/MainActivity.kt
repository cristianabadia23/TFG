 package com.example.proyectofinal

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectofinal.ui.intro.IntroActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView.visibility = View.INVISIBLE
        textView.visibility = View.INVISIBLE
        textView2.visibility = View.INVISIBLE
        preferences = this?.getPreferences(Context.MODE_PRIVATE)
        editor = preferences.edit()
        if (preferences.getBoolean("visto",true)){
            editor.putBoolean("visto",false)
            Log.e("IMA","Animacion larga")
            animacionLarga()
        }
        else{
            Log.e("IMA","Animacion corta")
            animacionCorta()
        }
    }
    fun animacionLarga(){
        var animation = AnimationUtils.loadAnimation(this,R.anim.animation1)
        lottie1.startAnimation(animation)
        var handler = Handler().postDelayed({
            lottie1.visibility = View.INVISIBLE
            imageView.visibility = View.VISIBLE
            textView.visibility = View.VISIBLE
            textView2.visibility = View.VISIBLE
            textView.startAnimation(animation)
            textView2.startAnimation(animation)
            imageView.startAnimation(animation)
        }, 6000)
        var handler2 = Handler().postDelayed({
            editor.apply()
            startActivity(Intent(this, IntroActivity::class.java))
            finish()
        }, 10000)
    }
    fun animacionCorta(){
        lottie1.visibility = View.INVISIBLE
        textView.visibility = View.VISIBLE
        textView2.visibility = View.VISIBLE
        imageView.visibility = View.VISIBLE
        var handler2 = Handler().postDelayed({
            startActivity(Intent(this, IntroActivity::class.java))
            finish()
        }, 2000)
    }

}