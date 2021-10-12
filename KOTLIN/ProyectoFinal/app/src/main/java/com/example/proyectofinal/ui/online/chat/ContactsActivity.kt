package com.example.proyectofinal.ui.online.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectofinal.R
import com.example.proyectofinal.models.User
import com.example.proyectofinal.providers.UserProvider
import com.example.proyectofinal.ui.adapter.UserAdapter
import com.example.proyectofinal.ui.online.MainOnlineActivity
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_contacts.*

class ContactsActivity : AppCompatActivity() {
    private val userProvider: UserProvider = UserProvider()
    private  var userAdapter: UserAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        mainonline.setOnClickListener{
            startActivity(Intent(this@ContactsActivity,MainOnlineActivity::class.java))
        }
        initRecycleView()
    }
    private fun initRecycleView() {
        val query : Query = userProvider.getCollectionReference()
        val firestoreRecyclerOption : FirestoreRecyclerOptions<User> = FirestoreRecyclerOptions
                .Builder<User>()
                .setQuery(query, User::class.java)
                .build()
        userAdapter = UserAdapter(firestoreRecyclerOption,this)
        rUsuarios.layoutManager = LinearLayoutManager(this) //    java.lang.NullPointerException: recycleview must not be null
        rUsuarios.adapter = userAdapter
    }
    override fun onStart() {
        super.onStart()
        userAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        userAdapter?.stopListening()
    }
}