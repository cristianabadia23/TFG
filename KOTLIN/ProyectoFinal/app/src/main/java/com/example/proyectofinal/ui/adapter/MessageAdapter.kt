package com.example.proyectofinal.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.models.Message
import com.example.proyectofinal.providers.AuthProvider
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.messageadapter.view.*
import java.text.SimpleDateFormat
import java.util.*


class MessageAdapter(options: FirestoreRecyclerOptions<Message>, context: Context) : FirestoreRecyclerAdapter<Message, MessageAdapter.MessageAdapterVH>(
    options
) {

    var context = context

    class MessageAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mMensaje = itemView.bmensaje
        var mFecha = itemView.fecha
        var mCheck = itemView.check
        var linearLayoutMessage = itemView.linearLayoutMessage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapterVH {
        return MessageAdapterVH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.messageadapter,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MessageAdapterVH, position: Int, model: Message) {
        var doc: DocumentSnapshot = snapshots.getSnapshot(position)
        var messageid = doc.id
        var mensaje: Message? = snapshots.getSnapshot(position).toObject(Message::class.java)
        if (mensaje != null) {
            if (mensaje.mBody.isNotEmpty()){
                holder.mMensaje.text = model.mBody
                holder.mFecha.text = convertLongToTime(model.timestamp)
                if (AuthProvider().getUid() == model.idEnviador){
                    val params = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                    params.setMargins(150, 0, 0, 0)
                    holder.linearLayoutMessage.layoutParams = params
                    holder.linearLayoutMessage.setPadding(30, 20, 25, 20)
                    holder.linearLayoutMessage.background = context.resources.getDrawable(R.drawable.mensaje)
                    holder.mCheck.visibility = View.VISIBLE
                    holder.mMensaje.setTextColor(Color.DKGRAY)
                    holder.mFecha.setTextColor(Color.LTGRAY)
                }
                else{
                    val params = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                    params.setMargins(0, 0, 150, 0)
                    holder.linearLayoutMessage.layoutParams = params
                    holder.linearLayoutMessage.setPadding(30, 20, 30, 20)
                    holder.linearLayoutMessage.background = context.resources.getDrawable(R.drawable.mensaje2)
                    holder.mCheck.setVisibility(View.GONE)
                    holder.mMensaje.setTextColor(Color.WHITE)
                    holder.mFecha.setTextColor(Color.LTGRAY)
                }
            }


        }
    }
    @SuppressLint("SimpleDateFormat")
    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return format.format(date)
    }


}