package com.example.proyectofinal.ui.online.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectofinal.R
import com.example.proyectofinal.models.Chat
import com.example.proyectofinal.providers.AuthProvider
import com.example.proyectofinal.providers.ChatProvider
import com.example.proyectofinal.providers.UserProvider
import com.example.proyectofinal.ui.adapter.ChatAdapter
import com.example.proyectofinal.ui.online.chat.ContactsActivity
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_chat.*


class ChatFragment : Fragment() {
    private  var chatAdapter: ChatAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bfchat.setOnClickListener{
            startActivity(Intent(context,ContactsActivity::class.java))
        }

         initRecycleView()

    }

    override fun onStart() {
        super.onStart()
        chatAdapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        chatAdapter!!.stopListening()
    }
    private fun initRecycleView() {
        val query : Query? = AuthProvider().getUid()?.let { ChatProvider().getAll(it) }
        val firestoreRecyclerOption : FirestoreRecyclerOptions<Chat> = FirestoreRecyclerOptions
            .Builder<Chat>()
            .setQuery(query!!, Chat::class.java)
            .build()
        chatAdapter = context?.let { ChatAdapter(firestoreRecyclerOption, it) }
        rChat.layoutManager = LinearLayoutManager(context)
        rChat.adapter = chatAdapter
    }
}