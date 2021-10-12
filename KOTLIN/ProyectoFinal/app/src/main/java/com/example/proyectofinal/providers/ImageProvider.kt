package com.example.proyectofinal.providers

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream

class ImageProvider {
    private var mStorage: FirebaseStorage
    private var storageRef : StorageReference
    init {
        mStorage = FirebaseStorage.getInstance()
        storageRef = mStorage.reference
    }

    fun save(mostrar: ImageView, nombre:String): UploadTask {
        val bitmap = (mostrar.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        var uploadTask = storageRef.child("users/${nombre.toString()}").putBytes(data)
        return uploadTask
    }

}