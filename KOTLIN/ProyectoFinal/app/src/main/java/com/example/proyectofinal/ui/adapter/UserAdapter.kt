package com.example.proyectofinal.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectofinal.R
import com.example.proyectofinal.models.User
import com.example.proyectofinal.ui.online.chat.OtherUserInfoActivity
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.useradapter.view.*

class UserAdapter(options: FirestoreRecyclerOptions<User>,context:Context) : FirestoreRecyclerAdapter<User, UserAdapter.UserAdapterVH>(options) {
    var context = context

    class UserAdapterVH (itemView: View) : RecyclerView.ViewHolder(itemView){
        var nickname = itemView.nickuser
        var imageuser = itemView.circleuser
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapterVH {
        return UserAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.useradapter,parent,false))
    }

    override fun onBindViewHolder(holder: UserAdapterVH, position: Int, model: User) {
        holder.nickname.text = model.nick
        Glide.with(context).load(model.imageURI).into(holder.imageuser)
        holder.itemView.setOnClickListener{
            var intent = Intent(context, OtherUserInfoActivity::class.java)
            Toast.makeText(context,"Cargando usuario",Toast.LENGTH_LONG).show()
            intent.putExtra("ID",model.id)
            context.startActivity(intent)
        }
    }
}