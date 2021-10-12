package com.example.proyectofinal.ui.online.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.proyectofinal.R
import com.example.proyectofinal.models.User
import com.example.proyectofinal.providers.AuthProvider
import com.example.proyectofinal.providers.ImageProvider
import com.example.proyectofinal.providers.UserProvider
import com.opensooq.supernova.gligar.GligarPicker
import kotlinx.android.synthetic.main.activity_other_user_info.*
import kotlinx.android.synthetic.main.fragment_perfil.*
import kotlinx.android.synthetic.main.fragment_perfil.myPuntos
import kotlinx.android.synthetic.main.fragment_perfil.myVictorias
import kotlinx.android.synthetic.main.fragment_perfil.myderrotas
import kotlinx.android.synthetic.main.fragment_perfil.mynivel

class PerfilFragment : Fragment() {
    private val PICKER_REQUEST_CODE = 30
    lateinit var useraux:User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:
    Bundle?): View? {
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AuthProvider().getUid()?.let {
            UserProvider().read(it).addOnSuccessListener { it->
                useraux = it.toObject(User::class.java)!!
                if (useraux != null) {
                    useraux.cambiarRango()
                    editMyName.setText(useraux.nick)
                    mynivel.text = "Nivel:\t\t${useraux.nivel}"
                    myPuntos.text = "Puntos:\t\t${useraux.puntos}"
                    myVictorias.text = "Victorias:\t\t${useraux.victorias}"
                    myderrotas.text = "Derrotas:\t\t${useraux.derrotas}"
                    Glide.with(this).load(useraux.imageURI).into(circlemyimage)
                }
            }
        }
        mynameUpdate.setOnClickListener{
            useraux.nick = editMyName.text.toString()
            useraux.cambiarRango()
            AuthProvider().getUid()?.let { it1 ->
                UserProvider().create(it1,useraux)?.addOnSuccessListener {
                    Toast.makeText(context,"Nombre cambiado",Toast.LENGTH_SHORT).show()
                }?.addOnFailureListener{
                    Toast.makeText(context,"El error al cambiar el nombre",Toast.LENGTH_SHORT).show()
                }
            }
        }
        myeditfoto.setOnClickListener {
            activity?.let { it1 ->
                GligarPicker().limit(1).disableCamera(false)
                        .cameraDirect(false).requestCode(PICKER_REQUEST_CODE)
                        .withActivity(it1).show()
            }
        }
    }
}