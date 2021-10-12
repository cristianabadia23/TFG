package com.example.proyectofinal.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectofinal.R
import com.example.proyectofinal.models.User
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.rankingadapter.view.*

class RankingAdapter(options: FirestoreRecyclerOptions<User>,context: Context) : FirestoreRecyclerAdapter<User, RankingAdapter.RankingAdapterVH>(options) {
    var context = context
    class RankingAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nick = itemView.trnick
        var rango = itemView.trango
        var victorias = itemView.tvictoria
        var puntos = itemView.tpuntos
        var foto = itemView.fperfil
        var derrotas = itemView.tderrotas
        var empates = itemView.tempates
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingAdapterVH {
        return RankingAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.rankingadapter,parent,false))
    }

    override fun onBindViewHolder(holder: RankingAdapterVH, position: Int, model: User) {
        model.cambiarRango()
        holder.nick.text = model.nick
        holder.derrotas.text = model.derrotas.toString()
        holder.puntos.text = model.puntos.toString()
        holder.rango.text = model.nivel
        holder.victorias.text = model.victorias.toString()
        holder.empates.text = model.empates.toString()
        Glide.with(context).load(model.imageURI).into(holder.foto)
    }

}