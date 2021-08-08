package com.example.beritaapp.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.beritaapp.R
import com.example.beritaapp.ui.*
import com.google.firebase.auth.FirebaseAuth
import com.theartofdev.edmodo.cropper.CropImage
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
        icLogout.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Konfirmasi")
                .setMessage("Apakah anda ingin keluar?")
                .setPositiveButton("Keluar", DialogInterface.OnClickListener { dialogInterface, i ->
                    deleteProduct()
                })
                .setNegativeButton("Batal", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })
        }
        btnUbahUser.setOnClickListener {
            startActivity(Intent(context, EditUser::class.java))
        }

    }

    private fun deleteProduct() {
        auth.signOut()
        Toast.makeText(context, "successfully logout", Toast.LENGTH_LONG).show()
        intent()
    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        tvUsername.text = user?.displayName
        context?.let {
            Glide.with(it)
                .load(user?.photoUrl)
                .into(imgAccount)
        }
        tvEmail.text = user?.email
    }

    fun intent(){
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
    }
}