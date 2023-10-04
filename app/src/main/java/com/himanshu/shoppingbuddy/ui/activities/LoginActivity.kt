package com.himanshu.shoppingbuddy.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.himanshu.shoppingbuddy.Firestore.FirestoreClass
import com.himanshu.shoppingbuddy.R
import com.himanshu.shoppingbuddy.databinding.ActivityLoginBinding
import com.himanshu.shoppingbuddy.models.User
import com.himanshu.shoppingbuddy.utils.Constants
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        super.onCreate(savedInstanceState)

        @Suppress("Deprecation")
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        sharedPreferences = getSharedPreferences(resources.getString(R.string.app_name),Context.MODE_PRIVATE)

        val loggingStatus = sharedPreferences.getString(Constants.IS_LOGGED_IN,null)

        if(loggingStatus == "true") {
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
            finish()
        }

        //sent to LogInActivity
        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // login after validating userDetails
        binding.btnLogin.setOnClickListener {
            logInRegisteredUser( )
        }

//        sent to ForgotPasswordActivity
        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity((intent))
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
//                showErrorSnackBar("Your details are valid.", false)
                true
            }
        }
    }

    private fun logInRegisteredUser() {

        if (validateLoginDetails()) {

            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Get the text from editText and trim the space
            val email = binding.etEmail.text.toString().trim { it <= ' ' }
            val password = binding.etPassword.text.toString().trim { it <= ' ' }

            // Log-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    // Hide the progress dialog
//                    hideProgressDialog()

                    if (task.isSuccessful) {

                        sharedPreferences = getSharedPreferences(resources.getString(R.string.app_name),Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor?.putString(Constants.IS_LOGGED_IN, "true")
                        editor?.apply()

                        com.himanshu.shoppingbuddy.Firestore.FirestoreClass().getUserDetails(this)

                    } else {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    fun userLoggedInSuccess(user: User) {

        // Hide the progress dialog.
        hideProgressDialog()

        // Print the user details in the log as of now.
        Log.i("First Name: ", user.firstName)
        Log.i("Last Name: ", user.lastName)
        Log.i("Email: ", user.email)

        if (user.profileCompleted == 0) {
            // If the user profile is incomplete then launch the UserProfileActivity upon login
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
            startActivity(intent)
        } else {

            // Redirect the user to Main Screen after log in if profile is completed
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        }
        finish()
    }

    override fun onResume() {
        super.onResume()

        if(Constants.IS_LOGGED_IN == "true"){
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
            finish()
        }

    }
}