package com.example.proyectofinal.ui.online.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.bumptech.glide.Glide
import com.example.proyectofinal.R
import com.example.proyectofinal.models.Chat
import com.example.proyectofinal.models.Message
import com.example.proyectofinal.providers.AuthProvider
import com.example.proyectofinal.providers.ChatProvider
import com.example.proyectofinal.providers.MessageProvider
import com.example.proyectofinal.providers.UserProvider
import com.example.proyectofinal.ui.adapter.MessageAdapter
import com.example.proyectofinal.ui.online.MainOnlineActivity
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.action_bar_toolbar.*
import kotlinx.android.synthetic.main.action_chat_toolbar.*
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_contacts.*
import java.util.*


class ChatActivity : AppCompatActivity() {
    var idUser1: String =""
    var idUser2: String =""
    var chatid:String = ""

    var messageAdapter:MessageAdapter? = null
    lateinit var mLayoutManager: LinearLayoutManager
    var messageProvider:MessageProvider = MessageProvider()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        showCustomToolbar(R.layout.action_chat_toolbar)
        val extras = intent.extras
        idUser1 = extras?.getString("IDUSER1").toString()
        idUser2 = extras?.getString("IDUSER2").toString()
        chatid = extras?.getString("IDCHAT").toString()
        if (chatid == null){
            checkifChatExist()
        }
        if (idUser1 !=null && idUser2 != null) {
            checkifChatExist()
        }
        
        else{
            Toast.makeText(this, "FALLO AL MOSTRAR EL CHAT", Toast.LENGTH_LONG).show()
        }
        bsend.setOnClickListener{
            sendMessage()
        }
        salidacirculo.setOnClickListener{
            startActivity(Intent(this@ChatActivity, MainOnlineActivity::class.java))
        }
        initRecycleView()
        getUserInfo()
      
    }

    override fun onStart() {
        super.onStart()
        messageAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        messageAdapter?.stopListening()
    }

    private fun getUserInfo() {
        var idOtherUser = ""
        if (AuthProvider().getUid() == idUser1){
            idOtherUser = idUser2
        }
        else{
            idOtherUser = idUser1
        }
        UserProvider().read(idOtherUser).addOnSuccessListener {
            if (it.exists()){
                nombreuser.text = it.getString("nick")
                Glide.with(this).load(it.getString("imageURI")).into(usercirculo)
            }
        }.addOnFailureListener{
            Toast.makeText(this, "ERROR al recuperar el usuario", Toast.LENGTH_LONG).show()
        }

    }

    private fun sendMessage() {
        if (tmessage.text.isNotEmpty()){
            var mensaje = Message()
            mensaje.idChat =chatid
            if (AuthProvider().getUid() == idUser1){
                mensaje.idEnviador = idUser1
                mensaje.idRecibidor = idUser2
            }
            else{
                mensaje.idEnviador = idUser2
                mensaje.idRecibidor = idUser1
            }
            mensaje.timestamp = Date().time
            mensaje.isVisto = false
            mensaje.mBody = tmessage.text.toString()
            MessageProvider().create(mensaje)?.addOnCompleteListener{ it->
                if (it.isSuccessful){
                    tmessage.setText("")
                    Toast.makeText(this, "Mensaje Creado", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this, "Mensaje No Creado", Toast.LENGTH_LONG).show()
                }
            }
            messageAdapter?.notifyDataSetChanged()
            messageAdapter!!.registerAdapterDataObserver(object : AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    val numberMessage: Int = messageAdapter!!.getItemCount()
                    val lastMessagePosition: Int =
                        mLayoutManager.findLastCompletelyVisibleItemPosition()
                    if (lastMessagePosition == -1 || positionStart >= numberMessage - 1 && lastMessagePosition == positionStart - 1) {
                        rMessage.scrollToPosition(positionStart)
                    }
                }
            })
        }
    }

    private fun showCustomToolbar(resource: Int) {
        var toolbar: androidx.appcompat.widget.Toolbar? = toolbar
        setSupportActionBar(toolbar)//linea fallo
        var actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title=""
            actionBar.setDisplayShowCustomEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
            var inflarte :LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var actionBarview = inflarte.inflate(resource, null)
            actionBar.customView = actionBarview
        }
    }

    fun checkifChatExist(){
        ChatProvider().getChatByUser1AndUser2(idUser1, idUser2)?.get()?.addOnSuccessListener { it->
            if (it.isEmpty){
                Toast.makeText(this, "Creando chat ...", Toast.LENGTH_LONG).show()
                crearChat()
            }
            else{
                chatid = it.documents[0].id
                Toast.makeText(this, "Mostrando chat", Toast.LENGTH_LONG).show()
            }
        }?.addOnFailureListener {
            Toast.makeText(this, "Error al crear el chat", Toast.LENGTH_LONG).show()
        }
    }

    fun crearChat(){
        Toast.makeText(this, "Chat creado", Toast.LENGTH_LONG).show()
        val ids = listOf(idUser1, idUser2)
        var chat = Chat(idUser1 + idUser2, false, Date().time, idUser1, idUser2, ids)
        chatid = idUser1+idUser2
        ChatProvider().create(chat)
        Toast.makeText(this, "Chat creado", Toast.LENGTH_LONG).show()
        finish()
        startActivity(Intent(this@ChatActivity, MainOnlineActivity::class.java))
    }

    private fun initRecycleView() {
        val query : Query? = messageProvider.getMessageByChat(chatid)
        val firestoreRecyclerOption : FirestoreRecyclerOptions<Message> = FirestoreRecyclerOptions
                .Builder<Message>()
                .setQuery(query!!, Message::class.java)
                .build()
        messageAdapter = MessageAdapter(firestoreRecyclerOption, this)
        mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.stackFromEnd = true
        rMessage.layoutManager = mLayoutManager
        rMessage.adapter = messageAdapter
    }
}