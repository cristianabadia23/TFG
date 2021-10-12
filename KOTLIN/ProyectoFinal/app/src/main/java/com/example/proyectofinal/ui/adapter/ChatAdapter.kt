package com.example.proyectofinal.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectofinal.R
import com.example.proyectofinal.models.Chat
import com.example.proyectofinal.models.User
import com.example.proyectofinal.providers.AuthProvider
import com.example.proyectofinal.providers.UserProvider
import com.example.proyectofinal.ui.offline.SelectorActivity
import com.example.proyectofinal.ui.online.chat.ChatActivity
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.action_chat_toolbar.*
import kotlinx.android.synthetic.main.chatadapter.view.*
import org.jetbrains.annotations.NotNull

class ChatAdapter (options: FirestoreRecyclerOptions<Chat>, context: Context) : FirestoreRecyclerAdapter<Chat, ChatAdapter.ChatAdapterVH>(options) {

    var context = context

    class ChatAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cnick = itemView.cnick
        var cbody = itemView.cbody
        var imagenotrousuario = itemView.imageOtherUserChat
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ChatAdapterVH {
        return ChatAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.chatadapter,parent,false))
    }

    override fun onBindViewHolder(holder: ChatAdapter.ChatAdapterVH, position: Int, @NotNull model: Chat) {
        holder.cbody.text ="PROXIMAMENTE"
        var doc:DocumentSnapshot = snapshots.getSnapshot(position)
        var chatid = doc.id
        var chat: Chat? = snapshots.getSnapshot(position).toObject(Chat::class.java)
        if (chat != null) {
            if (AuthProvider().getUid() == model.idUser1){
                model.idUser2?.let { getUserInfo(it,holder) }
            } else{
                model.idUser1?.let { getUserInfo(it,holder) }
            }
        }
        holder.itemView.setOnClickListener{
            model.idUser1?.let { it1 -> model.idUser2?.let { it2 ->
                    getChatActivity(chatid, it1, it2)
                }
            }
        }

    }

    private fun getChatActivity(chatid:String,idUser1:String,idUser2:String) {
        var intent:Intent = Intent(context,ChatActivity::class.java)
        intent.putExtra("IDUSER1",idUser1)
        intent.putExtra("IDUSER2",idUser2)
        intent.putExtra("IDCHAT",chatid)
        context.startActivity(intent)
    }

    private fun getUserInfo(id: String, holder: ChatAdapter.ChatAdapterVH) {
        UserProvider().read(id).addOnSuccessListener { it->
            if (it.exists()){
                var username = it.getString("nick")
                holder.cnick.text = username
                if (it.getString("imageURI").toString().isNotEmpty()){
                    Glide.with(context).load(it.getString("imageURI")).into(holder.imagenotrousuario)
                }
            }
        }.addOnFailureListener{
            Toast.makeText(context,"Fallo al crear el card",Toast.LENGTH_LONG).show()
        }
    }
}