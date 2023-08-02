package com.himanshu.shoppingbuddy.ui.activities

import android.content.Context
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.himanshu.shoppingbuddy.R
import com.himanshu.shoppingbuddy.databinding.ActivityMainBinding
import com.himanshu.shoppingbuddy.utils.Constants

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