package com.example.proyectofinal.ui.online

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.proyectofinal.R
import com.example.proyectofinal.providers.AuthProvider
import com.example.proyectofinal.providers.ImageProvider
import com.example.proyectofinal.providers.UserProvider
import com.example.proyectofinal.ui.online.fragments.*
import com.opensooq.supernova.gligar.GligarPicker
import kotlinx.android.synthetic.main.activity_main_online.*
import kotlinx.android.synthetic.main.fragment_perfil.*

class MainOnlineActivity : AppCompatActivity() {

    val authProvider: AuthProvider = AuthProvider()
    val userProvider:UserProvider = UserProvider()
    private val PICKER_REQUEST_CODE = 30
    lateinit var useraux:com.example.proyectofinal.models.User
    override fun onCreate(savedInstanceState: Bundle?) {
        val gameFragment = GameBlankFragment()
        val chatFragment = ChatFragment()
        val perfilFragment = PerfilFragment()
        val rankingFragment = RankingFragment()
        val logOutFragment = LogOutFragment()
        getUser()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_online)
        bottom_navigation_view.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_juego -> {
                    openFragment(gameFragment)
                    true
                }
                R.id.action_chat -> {
                    openFragment(chatFragment)
                    true
                }
                R.id.action_perfil -> {

                    openFragment(perfilFragment)
                    true
                }
                R.id.action_ranking ->{
                    openFragment(rankingFragment)
                    true
                }
                R.id.action_logout->{
                    openFragment(logOutFragment)
                    true
                }
                else -> false
            }
        }
        bottom_navigation_view.selectedItemId = R.id.action_juego
    }
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }
        Toast.makeText(this,requestCode.toString(),Toast.LENGTH_LONG).show()
        when (requestCode) {
            PICKER_REQUEST_CODE -> {
                Log.i("AVISO","Codigo aceptadoa")
                val imagesList = data?.extras?.getStringArray(GligarPicker.IMAGES_RESULT)
                if (!imagesList.isNullOrEmpty()) {
                    circlemyimage.setImageURI(imagesList[0].toUri())
                    Log.i("AVISO","Foto cambiada")
                    var user = useraux
                    if (user != null) {
                        Log.i("AVISO","Usuario recuperado")
                        ImageProvider().save(circlemyimage, user.nick).addOnFailureListener{
                            Toast.makeText(this, "Error al subir la foto", Toast.LENGTH_LONG).show()
                        }.addOnSuccessListener { taskSnapshot ->
                            Log.i("AVISO","Foto cambiada")
                            Toast.makeText(this, "Subiendo foto", Toast.LENGTH_LONG)
                            taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                                var imageURI = it.toString()
                                Log.i("Aviso",imageURI)
                                Toast.makeText(this, "Imagen guardada", Toast.LENGTH_LONG).show()
                                user.imageURI = imageURI
                                authProvider.getUid()?.let { it1 ->
                                    userProvider.create(it1,user)?.addOnSuccessListener {
                                        Toast.makeText(this,"Imagen cambiada",Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }
    fun getUser() {
        authProvider.getUid()?.let {
            userProvider.read(it).addOnSuccessListener { it->
                useraux = it.toObject(com.example.proyectofinal.models.User::class.java)!!
            }
        }
    }


}
