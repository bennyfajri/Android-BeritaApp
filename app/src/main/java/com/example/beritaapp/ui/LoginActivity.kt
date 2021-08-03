package com.example.beritaapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.beritaapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        btnLogin.setOnClickListener {
            loginActivity()
        }
        tvToRegister.setOnClickListener(){
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }
        icBackLogin.setOnClickListener {
            finish()
        }

    }

    private fun loginActivity() {
        val email = etEmailLogin.text.toString()
        val password = etPasswordLogin.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val login = auth.signInWithEmailAndPassword(email, password).await()
                    if(login.user != null){
                        withContext(Dispatchers.Main){
                            Toast.makeText(applicationContext, "Successfully login", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }catch (e: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}