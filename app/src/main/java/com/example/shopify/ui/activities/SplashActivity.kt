package com.example.shopify.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.shopify.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        @Suppress("Deprecation")
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        @Suppress("Deprecation")
        Handler().postDelayed({
                 val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        },2000)
    }
}