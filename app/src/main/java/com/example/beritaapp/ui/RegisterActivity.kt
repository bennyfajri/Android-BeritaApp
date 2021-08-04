package com.example.beritaapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.beritaapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.bottom_sheet_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

const val REQUEST_CODE_SIGN_IN = 0

class RegisterActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        btnRegister.setOnClickListener {
            registerUser()
            showBottonSheet()
        }
        icBack.setOnClickListener {
            finish()
        }
        tvToLogin.setOnClickListener {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }
        btnRegisterGoogle.setOnClickListener {
            val option = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build()
            val signInClient = GoogleSignIn.getClient(this, option)
            signInClient.signInIntent.also {
                startActivityForResult(it, REQUEST_CODE_SIGN_IN)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            account?.let {
                googleAuthForFirebase(it)
            }
        }
    }

    private fun googleAuthForFirebase(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithCredential(credentials).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegisterActivity, "Successfully sign in", Toast.LENGTH_LONG)
                        .show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun registerUser() {
        val email = etEmailRegister.text.toString()
        val password = etPasswordRegister.text.toString()
//        val random = (0..10000).random()
//        val username = "User$random"

//        val profileUpdates = UserProfileChangeRequest.Builder()
//            .setDisplayName(username)
//            .build()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val register = auth.createUserWithEmailAndPassword(email, password).await()

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun showBottonSheet() {
        val dialog = BottomSheetDialog(this@RegisterActivity)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_register, null)
        val btnSubmit = view.findViewById<MaterialButton>(R.id.btnAddNameRegister)
        val nama = view.findViewById<TextInputEditText>(R.id.etUsername)
        btnSubmit.setOnClickListener {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(nama.text.toString())
                .build()

            if (nama != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        auth.currentUser?.updateProfile(profileUpdates)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Berhasil membuat akun",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                dialog.dismiss()
                finish()
            }
        }
        dialog.setContentView(view)
        dialog.show()
    }


}