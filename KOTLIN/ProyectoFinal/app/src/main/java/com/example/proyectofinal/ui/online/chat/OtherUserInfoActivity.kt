package com.example.proyectofinal.ui.online.chat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.proyectofinal.R
import com.example.proyectofinal.models.User
import com.example.proyectofinal.providers.AuthProvider
import com.example.proyectofinal.providers.UserProvider
import com.example.proyectofinal.ui.online.MainOnlineActivity
import kotlinx.android.synthetic.main.activity_other_user_info.*

class OtherUserInfoActivity : AppCompatActivity() {
    lateinit var idOtroUsuario:String
    var id:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_user_info)
        val extras = intent.extras
        id = extras?.getString("ID")
        if (id != null) {
            UserProvider().read(id!!).addOnSuccessListener { it->
                val userAux  = it.toObject(User::class.java)
                if (userAux != null) {
                    idOtroUsuario = userAux.id
                    otherNick.text = "Nick:\t\t${userAux.nick}"
                    mynivel.text = "Nivel:\t\t${userAux.nivel}"
                    myPuntos.text = "Puntos:\t\t${userAux.puntos}"
                    myVictorias.text = "Victorias:\t\t${userAux.victorias}"
                    myderrotas.text="Derrotas:\t\t${userAux.derrotas}"
                    Glide.with(this).load(userAux.imageURI).into(myrFoto)

                }

            }.addOnFailureListener{
                Toast.makeText(this, "Error al cargar el usuario", Toast.LENGTH_LONG).show()
            }
        }
        else{
            Toast.makeText(this, "ID NULO", Toast.LENGTH_LONG).show()
        }

        if (id == AuthProvider().getUid()){
            bchat.visibility= View.GONE
        }

        bchat.setOnClickListener{
            generarChat()
        }
        iatras2.setOnClickListener{
            startActivity(Intent(this@OtherUserInfoActivity,MainOnlineActivity::class.java))
        }
    }

    private fun generarChat() {
        var intent:Intent = Intent(this@OtherUserInfoActivity,ChatActivity::class.java)
        intent.putExtra("IDUSER1",AuthProvider().getUid())
        intent.putExtra("IDUSER2",id)
        startActivity(intent)
    }
}