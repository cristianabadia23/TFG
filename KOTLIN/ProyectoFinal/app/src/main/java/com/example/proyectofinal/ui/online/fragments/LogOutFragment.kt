package com.example.proyectofinal.ui.online.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectofinal.R
import com.example.proyectofinal.providers.AuthProvider
import com.example.proyectofinal.ui.offline.OfflineMainActivity
import kotlinx.android.synthetic.main.fragment_log_out.*

class LogOutFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_out, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bsi.setOnClickListener{
            AuthProvider().logout()
            startActivity(Intent(activity,OfflineMainActivity::class.java))
            activity?.finish()

        }
        boffliner.setOnClickListener{
            startActivity(Intent(activity,OfflineMainActivity::class.java))
        }
    }

}