package com.himanshu.shoppingbuddy.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.core.app.ActivityCompat.startActivityForResult

object Constants {
    const val EXTRA_MY_ORDER_DETAILS: String = "extra_my_order_details"
    const val FIRST_NAME: String = "firstName"
    const val LAST_NAME: String = "lastName"
    const val IMAGE: String = "image"
    const val USERS :String = "users"
    const val Shopify_preferences: String = "MyShopPalPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE: Int = 1
    const val PICK_IMAGE_REQUEST_CODE: Int = 2
    const val COMPLETE_PROFILE: String = "profileCompleted"
    const val PRODUCT_IMAGE : String = "Product_Image"
    const val EXTRA_PRODUCT_ID : String = "extra_product_id"
    const val EXTRA_PRODUCT_OWNER_ID: String = "extra_product_owner_id"
    const val DEFAULT_CART_QUANTITY : String = "1"
    const val CART_ITEMS : String = "cart_items"
    const val SOLD_PRODUCTS : String = "sold_products"

    const val PRODUCTS : String = "products"
    const val USER_ID : String = "user_id"
    const val PRODUCT_ID : String = "product_id"
    // Constant variables for Gender
    //
    const val MALE: String = "Male"
    const val FEMALE: String = "Female"

    // Firebase database field names
    const val MOBILE: String = "mobile"
    const val GENDER: String = "gender"
    const val USER_PROFILE_IMAGE = "User_Profile_Image"
    const val CART_QUANTITY: String = "cart_quantity"
    const val STOCK_QUANTITY: String = "stock_quantity"
    const val HOME: String = "Home"
    const val OFFICE: String = "Office"
    const val OTHER: String = "Other"
    const val ADDRESSES: String = "addresses"
    const val EXTRA_ADDRESS_DETAILS: String = "AddressDetails"

    const val EXTRA_SELECT_ADDRESS: String = "extra_select_address"
    const val EXTRA_SELECTED_ADDRESS: String = "extra_selected_address"


    // TODO Step 11: Declare a global constant variable to notify the add address.
    // START
    const val ADD_ADDRESS_REQUEST_CODE: Int = 121
    const val ORDERS : String ="orders"
    const val EXTRA_SOLD_PRODUCT_DETAILS : String = "extra_sold_product_details"

    //


    fun showImageChooser(activity: Activity) {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        /*
         * MimeTypeMap: Two-way map that maps MIME-types to file extensions and vice versa.
         *
         * getSingleton(): Get the singleton instance of MimeTypeMap.
         *
         * getExtensionFromMimeType: Return the registered extension for the given MIME type.
         *
         * contentResolver.getType: Return the MIME type of the given content URL.
         */


        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

}