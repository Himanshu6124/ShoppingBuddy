package com.himanshu.shoppingbuddy.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.himanshu.shoppingbuddy.R
import com.himanshu.shoppingbuddy.databinding.ActivityAddProductBinding
import com.himanshu.shoppingbuddy.Firestore.FirestoreClass
import com.himanshu.shoppingbuddy.models.Product
import com.himanshu.shoppingbuddy.utils.Constants
import com.himanshu.shoppingbuddy.utils.GlideLoader
import java.io.IOException

class AddProductActivity : BaseActivity() {
    private var mProductImageURL: String = ""
    private var mSelectedImageFileUri: Uri? = null

    private lateinit var binding : ActivityAddProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        binding.btnSubmit.setOnClickListener {

        if(validateProductDetails())
        {
//            showErrorSnackBar("Correct",false)
            uploadProductImage()
        }


        }


        binding.ivAddUpdateProduct.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                Constants.showImageChooser(this@AddProductActivity)
            } else {
                /*Requests permissions to be granted to this application. These permissions
                 must be requested in your manifest, they should not be granted to your app,
                 and they should have protection level*/
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

    }
    private fun validateProductDetails(): Boolean {
        return when {

            mSelectedImageFileUri == null -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_product_image), true)
                false
            }

            TextUtils.isEmpty(binding.etProductTitle.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_title), true)
                false
            }

            TextUtils.isEmpty(binding.etProductPrice.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_price), true)
                false
            }

            TextUtils.isEmpty(binding.etProductDescription.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_product_description),
                    true
                )
                false
            }

            TextUtils.isEmpty(binding.etProductQuantity.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_product_quantity),
                    true
                )
                false
            }
            else -> {
                true
            }
        }
    }

    private fun setupActionBar() {

        // set toolBar as actionBar
        setSupportActionBar(binding.toolbarAddProductActivity)

        val actionBar = supportActionBar

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }

        // lambda function when parameter is unused rename it to _
//        binding.toolbarRegisterActivity.setNavigationOnClickListener { _-> onBackPressed() }

        binding.toolbarAddProductActivity.setNavigationOnClickListener{ onBackPressed() }


        // anonymous Function
        binding.toolbarAddProductActivity.setNavigationOnClickListener({ onBackPressed() })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this@AddProductActivity)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == Constants.PICK_IMAGE_REQUEST_CODE
            && data!!.data != null
        ) {

            // Replace the add icon with edit icon once the image is selected.
            binding.ivAddUpdateProduct.setImageDrawable(
                ContextCompat.getDrawable(
                    this@AddProductActivity,
                    R.drawable.ic_vector_edit
                )
            )

            // The uri of selection image from phone storage.
            mSelectedImageFileUri = data.data!!

            try {
                // Load the product image in the ImageView.
                GlideLoader(this@AddProductActivity).loadUserPicture(
                    mSelectedImageFileUri!!,
                    binding.ivProductImage
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    fun imageUploadSuccess(imageURL: String) {

        // Hide the progress dialog
//        hideProgressDialog()
//        showErrorSnackBar("Done uploading",false)

        mProductImageURL= imageURL
        uploadProductDetails()



    }
    private fun uploadProductImage() {

        showProgressDialog(resources.getString(R.string.please_wait))

        com.himanshu.shoppingbuddy.Firestore.FirestoreClass().uploadImageToCloudStorage(
            this@AddProductActivity,
            mSelectedImageFileUri,
            Constants.PRODUCT_IMAGE
        )
    }

    private fun uploadProductDetails() {

        // Get the logged in username from the SharedPreferences that we have stored at a time of login.
        val username =
            this.getSharedPreferences(Constants.Shopify_preferences, Context.MODE_PRIVATE)
                .getString(Constants.LOGGED_IN_USERNAME, "")!!

        // Here we get the text from editText and trim the space
        val product = Product(
            FirestoreClass().getCurrentUserID(),
            username,
            binding.etProductTitle.text.toString().trim { it <= ' ' },
            binding.etProductPrice.text.toString().trim { it <= ' ' },
            binding.etProductDescription.text.toString().trim { it <= ' ' },
            binding.etProductQuantity.text.toString().trim { it <= ' ' },
            mProductImageURL
        )

        com.himanshu.shoppingbuddy.Firestore.FirestoreClass()
            .uploadProductDetails(this@AddProductActivity, product)
    }

    /**
     * A function to return the successful result of Product upload.
     */
    fun productUploadSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@AddProductActivity,
            resources.getString(R.string.product_uploaded_success_message),
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }
}
