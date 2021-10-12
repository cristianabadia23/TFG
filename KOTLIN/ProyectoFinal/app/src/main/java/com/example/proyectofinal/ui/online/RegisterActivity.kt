package com.example.proyectofinal.ui.online

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.net.toUri
import com.example.proyectofinal.R
import com.example.proyectofinal.models.User
import com.example.proyectofinal.providers.AuthProvider
import com.example.proyectofinal.providers.ImageProvider
import com.example.proyectofinal.providers.UserProvider
import com.opensooq.supernova.gligar.GligarPicker
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private val PICKER_REQUEST_CODE = 30
    private var imageURI:String = ""
    private val limite = 1
    private val authProvider = AuthProvider()
    private val userProvider = UserProvider()
    private val imageProvider = ImageProvider()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        ifoto.setOnClickListener{
            GligarPicker().limit(this.limite).disableCamera(false)
                    .cameraDirect(false).requestCode(PICKER_REQUEST_CODE)
                    .withActivity(this).show()

        }
        icamara.setOnClickListener{
            GligarPicker().limit(this.limite).disableCamera(false)
                    .cameraDirect(false).requestCode(PICKER_REQUEST_CODE)
                    .withActivity(this).show()
        }
        tinicsesion.setOnClickListener{
            startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
        }
        tinic.setOnClickListener{
            startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
        }
        bregister.setOnClickListener{
            if ((temail.text.isNotEmpty() && validarEmail(temail.text.toString())) &&
                tnick.text.isNotEmpty() && (tcontra.text.toString() == tcontrar.text.toString() && tcontra.text.length > 9)){
                imageProvider.save(ifoto, tnick.text.toString()).addOnFailureListener{
                    Toast.makeText(this, "Error al subir la foto", Toast.LENGTH_LONG).show()
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                        imageURI = it.toString()
                        Toast.makeText(this, "Imagen guardada", Toast.LENGTH_LONG).show()
                        authProvider.register(temail.text.toString(), tcontra.text.toString())
                                ?.addOnSuccessListener {
                                    Toast.makeText(this, "CORREO REGISTRADO", Toast.LENGTH_LONG).show()
                                    var usuario = AuthProvider().getUid()?.let { it1 ->
                                        User(
                                            it1,tnick.text.toString(), "PRINCIPIENTE",
                                            imageURI, 0, 0, 0, 0,false)
                                    }
                                    if (usuario != null) {
                                        this.crearUser(usuario)
                                    }
                                }
                    }
                }
            }
            else if(temail.text.isEmpty() || !validarEmail(temail.text.toString())){
                temail.error = "EMAIL NO VALIDO"
            }else if(tnick.text.isEmpty()){
                tnick.error = "NICK NO VALIDO"
            }
            else if(tcontra.text.toString() != tcontrar.text.toString()){
                tcontra.error = "LAS CONTRASEÑAS NO COINCIDEN"
                tcontrar.error = "LAS CONTRASEÑAS NO COINCIDEN"
            }
            else if(tcontra.text.length < 9){
                tcontra.error = "CONTRASEÑA DEMASIADO CORTA"
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
                    ifoto.setImageURI(imagesList[0].toUri())
                }
            }
        }
    }
    private fun validarEmail(email: String):Boolean{
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }
    private fun crearUser(usuario: User){
        authProvider.getUid()?.let { it1 ->
            userProvider.create(it1, usuario)
                    ?.addOnSuccessListener {
                        Toast.makeText(this, "Usuario Registrado", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@RegisterActivity,MainOnlineActivity::class.java))
                    }?.addOnFailureListener{
                        Toast.makeText(this, "Usuario No Registrado", Toast.LENGTH_LONG).show()
                    }
        }
    }
}
