package com.example.proyectofinal.ui.online

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.proyectofinal.R
import com.example.proyectofinal.models.User
import com.example.proyectofinal.providers.AuthProvider
import com.example.proyectofinal.providers.ImageProvider
import com.example.proyectofinal.providers.UserProvider
import com.opensooq.supernova.gligar.GligarPicker
import kotlinx.android.synthetic.main.activity_completar_perfil.*



class CompletarPerfilActivity : AppCompatActivity() {
    var useraux: User?=null
    private val PICKER_REQUEST_CODE = 30
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completar_perfil)
        gatras.setOnClickListener{
            startActivity(Intent(this@CompletarPerfilActivity,LoginActivity::class.java))
        }
        gfoto.setOnClickListener{
            GligarPicker().limit(1).disableCamera(false)
                    .cameraDirect(false).requestCode(PICKER_REQUEST_CODE)
                    .withActivity(this).show()
        }
        gregistro.setOnClickListener{
            if (gnick.text.isNotEmpty()){
                ImageProvider().save(gfoto, gnick.text.toString()).addOnFailureListener{
                    Toast.makeText(this, "Error al subir la foto", Toast.LENGTH_LONG).show()
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                        val imageURI = it.toString()
                        Log.e("it",imageURI)
                        Toast.makeText(this, "Imagen guardada", Toast.LENGTH_LONG).show()
                        val uid: String? = AuthProvider().getUid()
                        val user = uid?.let { it1 -> User(it1,gnick.text.toString(),"",imageURI) }
                        if (uid != null && user != null) {
                            UserProvider().create(uid,user)?.addOnSuccessListener {
                                Toast.makeText(this,"Usuario creaado",Toast.LENGTH_LONG).show()
                                startActivity(Intent(this@CompletarPerfilActivity,MainOnlineActivity::class.java))
                            }?.addOnFailureListener{
                                Toast.makeText(this,"Error al crear el usuario",Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
            else{
                gnick.error = "Nick vacio"
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }
        when (requestCode) {
            PICKER_REQUEST_CODE -> {
                val imagesList = data?.extras?.getStringArray(GligarPicker.IMAGES_RESULT)
                if (!imagesList.isNullOrEmpty()) {
                    gfoto.setImageURI(imagesList[0].toUri())
                }
            }
        }
    }
}