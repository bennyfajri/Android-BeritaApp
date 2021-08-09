package com.example.beritaapp.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.example.beritaapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_edit_user.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


class EditUser : AppCompatActivity() {

    private val GALLERY_REQUEST_CODE = 1234
    private val TAG = "AppDebug"
    lateinit var auth: FirebaseAuth
    lateinit var i: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)
        supportActionBar?.hide()
        i = intent
        if(i.hasExtra("edit")){
            icBackEdit.visibility = View.VISIBLE
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 75, 0, 0)
            tvHello.setLayoutParams(params)
            val extras = intent.extras
            val byteArray = extras!!.getByteArray("picture")

            val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
            val image: ImageView = findViewById<View>(R.id.imgPhoto) as ImageView

            image.setImageBitmap(bmp)
            val nama = i.getStringExtra("name").toString()
            etUsername.setText(nama)
        }

        auth = FirebaseAuth.getInstance()
        btnAddNameRegister.setOnClickListener {
            editUser()
        }

        btnChoosePhoto.setOnClickListener {
            pickFromGallery()
        }

        icBackEdit.setOnClickListener {
            finish()
        }

    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

//    private fun choosePhoto() {
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(Intent.createChooser(intent, "title"), SELECT_IMAGE_CODE)
//    }

    private fun editUser() {
        val bitmap = imgPhoto.drawable.toBitmap()
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "tempimage1", null)
        val uri = Uri.parse(path)
        val username = etUsername.text.toString()
        if (username.isNotEmpty()) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .setPhotoUri(uri)
                .build()

            if (username.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val edit = auth.currentUser?.updateProfile(profileUpdates)
                        if (edit!!.isSuccessful) {
                            withContext(Dispatchers.Main) {
                                if(i.hasExtra("edit")){
                                    Toast.makeText(
                                        this@EditUser,
                                        "Berhasil ubah akun",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }else{
                                    Toast.makeText(
                                        this@EditUser,
                                        "Berhasil membuat akun",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@EditUser, e.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                if(i.hasExtra("edit")){
                    startActivity(Intent(this@EditUser, MainActivity::class.java))
                    finish()
                }else{
                    startActivity(Intent(this@EditUser, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun setImage(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .into(imgPhoto)
    }

    private fun launchImageCrop(uri: Uri) {
        CropImage.activity(uri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setCropShape(CropImageView.CropShape.RECTANGLE) // default is rectangle
            .start(this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            GALLERY_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.let { uri ->
                        launchImageCrop(uri)
                    }
                } else {
                    Log.e(TAG, "Image selection error: Couldn't select that image from memory.")
                }
            }

            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    setImage(result.uri)
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Log.e(TAG, "Crop error: ${result.getError()}")
                }
            }
        }

    }
}
