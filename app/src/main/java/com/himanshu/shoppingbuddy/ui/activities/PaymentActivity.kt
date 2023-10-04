package com.himanshu.shoppingbuddy.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.himanshu.shoppingbuddy.R
import com.himanshu.shoppingbuddy.databinding.ActivityPaymentBinding

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }


}