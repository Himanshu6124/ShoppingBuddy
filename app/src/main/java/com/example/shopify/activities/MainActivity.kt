package com.example.shopify.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.shopify.R
import com.example.shopify.databinding.ActivityMainBinding
import com.example.shopify.utils.Constants

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
                binding = DataBindingUtil.setContentView(this,R.layout.activity_main)


        val sharedPreferences =
            getSharedPreferences(Constants.Shopify_preferences, Context.MODE_PRIVATE)

        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!
        // Set the result to the tv_main.
        binding.tvMain.text= "The logged in user is $username."


    }
}