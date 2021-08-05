package com.example.beritaapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.beritaapp.R
import com.example.beritaapp.ui.LoginActivity
import com.example.beritaapp.ui.MainActivity
import com.example.beritaapp.ui.NewsViewModel
import com.example.beritaapp.ui.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_user.*

class UserFragment : Fragment(R.layout.fragment_user) {

    lateinit var viewModel : NewsViewModel
    lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        auth = FirebaseAuth.getInstance()
        if(auth.currentUser == null){
           intent()
        }
        btnLogout.setOnClickListener {
            auth.signOut()
            Toast.makeText(context, "successfully logout", Toast.LENGTH_LONG).show()
            intent()
        }
    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        tvUsername.text = user?.displayName
    }

    fun intent(){
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
    }
}