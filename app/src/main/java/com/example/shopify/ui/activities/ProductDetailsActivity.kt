package com.example.shopify.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.shopify.Firestore.FirestoreClass
import com.example.shopify.R
import com.example.shopify.databinding.ActivityProductDetailsBinding
import com.example.shopify.models.CartItems
import com.example.shopify.models.Product
import com.example.shopify.utils.GlideLoader

class ProductDetailsActivity : BaseActivity() {
    private lateinit var binding : ActivityProductDetailsBinding
    private lateinit var mProductDetails : Product
    private var mProductId : String =""
    private var mproductOwnerId : String =""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        binding.btnGoToCart.setOnClickListener{
            startActivity(Intent(this,CartListActivity::class.java))
        }



        if(intent.hasExtra(com.example.shopify.utils.Constants.EXTRA_PRODUCT_ID))
        {
            mProductId = intent.getStringExtra(com.example.shopify.utils.Constants.EXTRA_PRODUCT_ID)!!
//            Log.e("ghgh",mProductId)
            getProductDetails()
        }

        if(intent.hasExtra(com.example.shopify.utils.Constants.EXTRA_PRODUCT_OWNER_ID))
        {
            mproductOwnerId = intent.getStringExtra(com.example.shopify.utils.Constants.EXTRA_PRODUCT_OWNER_ID)!!
//            Log.e("ghgh",mProductId)

            if(FirestoreClass().getCurrentUserID() == mproductOwnerId)
            {
                binding.btnAddToCart.visibility = View.GONE
                binding.btnGoToCart.visibility =View.GONE
            }
            else
            {
                binding.btnAddToCart.visibility = View.VISIBLE
            }

        }


        binding.btnAddToCart.setOnClickListener {
            addToCart()
        }


    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarProductDetailsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }

        binding.toolbarProductDetailsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * A function to call the firestore class function that will get the product details from cloud firestore based on the product id.
     */
    private fun getProductDetails() {

        // Show the product dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of FirestoreClass to get the product details.
        FirestoreClass().getProductDetails(this@ProductDetailsActivity, mProductId)
    }

    fun productDetailsSuccess(product: Product) {
        mProductDetails = product
        // Hide Progress dialog.
//        hideProgressDialog()

        // Populate the product details in the UI.
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image,
            binding.ivProductDetailImage
        )

        binding.tvProductDetailsTitle.text = product.title
        binding.tvProductDetailsPrice.text = "₹ ${product.price}"
        binding.tvProductDetailsDescription.text = product.description
        binding.tvProductDetailsStockQuantity.text = product.stock_quantity

        if(product.stock_quantity.toInt() == 0){

            // Hide Progress dialog.
            hideProgressDialog()

            // Hide the AddToCart button if the item is already in the cart.
            binding.btnAddToCart .visibility = View.GONE

            binding.tvProductDetailsStockQuantity.text =
                resources.getString(R.string.lbl_out_of_stock)

            binding.tvProductDetailsStockQuantity.setTextColor(
                ContextCompat.getColor(
                    this@ProductDetailsActivity,
                    R.color.colorSnackBarError
                )
            )
        }else{

            // There is no need to check the cart list if the product owner himself is seeing the product details.
            if (FirestoreClass().getCurrentUserID() == product.user_id) {
                // Hide Progress dialog.
                hideProgressDialog()
            } else {
                FirestoreClass().checkIfItemExistInCart(this@ProductDetailsActivity, mProductId)
            }
        }


    }
    private fun addToCart() {

        val addToCart = CartItems(
            FirestoreClass().getCurrentUserID(),
            mproductOwnerId,
            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            com.example.shopify.utils.Constants.DEFAULT_CART_QUANTITY
        )
        showProgressDialog("adding to cart")
        FirestoreClass().addCartItems(this,addToCart)
    }
    fun addToCartSuccess() {
        // Hide the progress dialog.
        hideProgressDialog()

        Toast.makeText(
            this@ProductDetailsActivity,
            resources.getString(R.string.success_message_item_added_to_cart),
            Toast.LENGTH_SHORT
        ).show()

        // Hide the AddToCart button if the item is already in the cart.
        binding.btnAddToCart.visibility = View.GONE
        // Show the GoToCart button if the item is already in the cart. User can update the quantity from the cart list screen if he wants.
        binding.btnGoToCart.visibility = View.VISIBLE
    }

//    * A function to notify the success result of item exists in the cart.
    fun productExistsInCart() {

        // Hide the progress dialog.
        hideProgressDialog()

        // Hide the AddToCart button if the item is already in the cart.
        binding.btnAddToCart.visibility = View.GONE
        // Show the GoToCart button if the item is already in the cart. User can update the quantity from the cart list screen if he wants.
        binding.btnGoToCart.visibility = View.VISIBLE
    }
}