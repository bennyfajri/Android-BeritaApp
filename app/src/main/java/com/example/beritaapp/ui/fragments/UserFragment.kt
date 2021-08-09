package com.example.beritaapp.ui.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.beritaapp.R
import com.example.beritaapp.ui.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_user.*
import java.io.ByteArrayOutputStream


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
        icLogout.bringToFront()
        icLogout.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Konfirmasi")
                .setMessage("Apakah anda ingin keluar?")
                .setPositiveButton("Keluar", DialogInterface.OnClickListener { dialogInterface, i ->
                    signOut()
                })
                .setNegativeButton("Batal", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })
                .show()
        }
        btnUbahUser.setOnClickListener {
            val bitmap = imgAccount.drawable.toBitmap()
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray: ByteArray = stream.toByteArray()

            val intent = Intent(context, EditUser::class.java)
            intent.putExtra("edit", "edit")
            intent.putExtra("picture", byteArray)
            intent.putExtra("name", auth.currentUser?.displayName)
            startActivity(intent)
        }

    }

    private fun signOut() {
        auth.signOut()
        Toast.makeText(context, "successfully logout", Toast.LENGTH_LONG).show()
        intent()
    }

    override fun onResume() {
        super.onResume()
        setProfile()
    }

    override fun onPause() {
        super.onPause()
        setProfile()
    }

    private fun setProfile() {
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