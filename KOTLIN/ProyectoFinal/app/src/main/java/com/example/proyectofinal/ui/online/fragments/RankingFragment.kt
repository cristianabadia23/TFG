package com.example.proyectofinal.ui.online.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectofinal.R
import com.example.proyectofinal.models.User
import com.example.proyectofinal.providers.UserProvider
import com.example.proyectofinal.ui.adapter.RankingAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_ranking.*

class RankingFragment : Fragment() {
    private val userProvider: UserProvider = UserProvider()
    private  var rankingAdapter: RankingAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_ranking, container, false)
    }
    override fun onStart() {
        super.onStart()
        rankingAdapter!!.startListening()
    }
    override fun onStop() {
        super.onStop()
        rankingAdapter!!.stopListening()
    }
    fun initRecycleView(){
        val query : Query = userProvider.getCollectionReference().orderBy("puntos", Query.Direction.DESCENDING)
        val firestoreRecyclerOption : FirestoreRecyclerOptions<User> = FirestoreRecyclerOptions
                .Builder<User>()
                .setQuery(query,User::class.java)
                .build()
        rankingAdapter = context?.let { RankingAdapter(firestoreRecyclerOption, it) }
        recycleview.layoutManager = LinearLayoutManager(requireContext())
        recycleview.adapter = rankingAdapter
    }
}