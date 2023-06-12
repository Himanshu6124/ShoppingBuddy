package com.example.shopify.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.shopify.Firestore.FirestoreClass
import com.example.shopify.R
import com.example.shopify.databinding.ActivitySettingBinding
import com.example.shopify.models.User
import com.example.shopify.utils.Constants
import com.example.shopify.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth

class SettingActivity : BaseActivity() {
    private lateinit var binding : ActivitySettingBinding
    private lateinit var muserDetails : User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_setting)

        setupActionBar()
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@SettingActivity,LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.tvEdit.setOnClickListener {
            val intent = Intent(this@SettingActivity,UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,muserDetails)
            startActivity(intent)

        }

        binding.llAddress.setOnClickListener {
            val intent = Intent(this@SettingActivity,AddressListActivity::class.java)
            startActivity(intent)
        }


    }


    private fun setupActionBar() {


        setSupportActionBar(binding.toolbarSettingsActivity)

        val actionBar = supportActionBar

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }

        binding.toolbarSettingsActivity.setNavigationOnClickListener { onBackPressed() }

    }

    private fun getUserDetails() {

        // Show the progress dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class to get the user details from firestore which is already created.
        FirestoreClass().getUserDetails(this@SettingActivity)
    }
    // END

    // TODO Step 6: Create a function to receive the success result.
    // START
    /**
     * A function to receive the user details and populate it in the UI.
     */
    fun userDetailsSuccess(user: User) {

        muserDetails = user
        // TODO Step 9: Set the user details to UI.
        // START
        // Hide the progress dialog
        hideProgressDialog()

        // Load the image using the Glide Loader class.
        GlideLoader(this@SettingActivity).loadUserPicture(user.image, binding.ivUserPhoto)

        binding.tvName.text = "${user.firstName} ${user.lastName}"
        binding.tvGender.text = user.gender
        binding.tvEmail.text = user.email
        binding.tvMobileNumber.text = "${user.mobile}"
        // END
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }
}

