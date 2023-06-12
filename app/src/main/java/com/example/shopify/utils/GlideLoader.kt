package com.example.shopify.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.shopify.R
import java.io.IOException


class GlideLoader(val context: Context) {

    /**
     * A function to load image from URI for the user profile picture.
     */
    fun loadUserPicture(image: Any, imageView: ImageView) {
        try {
            // Load the user image in the ImageView.
            Glide
                .with(context)
                .load(Uri.parse(image.toString())) // URI of the image
//                .centerCrop() // Scale type of the image.
                .placeholder(R.drawable.ic_user_placeholder) // A default place holder if image is failed to load.
                .into(imageView) // the view in which the image will be loaded.
//            Log.i("jod","failed")
            Log.i("jod","passed")

        } catch (e: IOException) {
            e.printStackTrace()
            Log.i("jod","failed")
        }
    }

    fun loadProductPicture(image: Any, imageView: ImageView) {
        try {
            // Load the user image in the ImageView.
            Glide
                .with(context)
                .load(Uri.parse(image.toString())) // URI of the image
//                .centerCrop() // Scale type of the image.
//                .placeholder(R.drawable.ic_user_placeholder) // A default place holder if image is failed to load.
                .into(imageView) // the view in which the image will be loaded.
//            Log.i("jod","failed")
            Log.i("jod","passed")

        } catch (e: IOException) {
            e.printStackTrace()
            Log.i("jod","failed")
        }
    }
}