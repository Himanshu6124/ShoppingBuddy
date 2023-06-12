package com.example.shopify.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.Firestore.FirestoreClass
import com.example.shopify.R
import com.example.shopify.adapter.CartItemsListAdapter
import com.example.shopify.databinding.ActivityCheckoutBinding
import com.example.shopify.models.Address
import com.example.shopify.models.CartItems
import com.example.shopify.models.Order
import com.example.shopify.models.Product
import com.example.shopify.utils.Constants

class CheckoutActivity : BaseActivity() {
    // A global variable for the SubTotal Amount.
    private var mSubTotal: Double = 0.0
    // A global variable for the Total Amount.
    private var mTotalAmount: Double = 0.0
    private lateinit var binding : ActivityCheckoutBinding
    private var mAddressDetails: Address? = null
    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartItemsList: ArrayList<CartItems>
    private lateinit var mOrderDetails: Order


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_checkout)
        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)) {
            mAddressDetails =
                intent.getParcelableExtra<Address>(Constants.EXTRA_SELECTED_ADDRESS)!!


//                Log.d(mAddressDetails!!.,"jod")
        }


        if (mAddressDetails != null) {
            binding.tvCheckoutAddressType.text = mAddressDetails?.type
            binding.tvCheckoutFullName.text = mAddressDetails?.name
            binding.tvCheckoutAddress.text = "${mAddressDetails!!.address}, ${mAddressDetails!!.zipCode}"
            binding.tvCheckoutAdditionalNote.text = mAddressDetails?.additionalNote

            if (mAddressDetails?.otherDetails!!.isNotEmpty()) {
                binding.tvCheckoutOtherDetails.text = mAddressDetails?.otherDetails
            }
            binding.tvMobileNumber.text = mAddressDetails?.mobileNumber
        }

        binding.btnPlaceOrder.setOnClickListener {
            placeAnOrder()
        }
        getProductList()

    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarCheckoutActivity)
        val actionBar = supportActionBar

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }
        binding.toolbarCheckoutActivity.setNavigationOnClickListener { onBackPressed() }
    }
    private fun getProductList() {

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getAllProductsList(this@CheckoutActivity)
    }

    /**
     * A function to get the success result of product list.
     *
     * @param productsList
     */
    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        // TODO Step 8: Initialize the global variable of all product list.
        // START
        mProductsList = productsList
        // END

        // TODO Step 10: Call the function to get the latest cart items.
        // START
        getCartItemsList()
        // END
    }
    /**
     * A function to get the list of cart items in the activity.
     */
    private fun getCartItemsList() {

        FirestoreClass().getCartList(this@CheckoutActivity)
    }

    // TODO Step 11: Create a function to notify the success result of the cart items list from cloud firestore.
    // START
    /**
     * A function to notify the success result of the cart items list from cloud firestore.
     *
     * @param cartList
     */
    fun successCartItemsList(cartList: ArrayList<CartItems>) {

        // Hide progress dialog.
        hideProgressDialog()

        for (product in mProductsList) {
            for (cart in cartList) {
                if (product.product_id == cart.product_id) {
                    cart.stock_quantity = product.stock_quantity
                }
            }
        }

        // TODO Step 13: Initialize the cart list.
        // START
        mCartItemsList = cartList

        binding.rvCartListItems.layoutManager = LinearLayoutManager(this@CheckoutActivity)
        binding.rvCartListItems.setHasFixedSize(true)

        val cartListAdapter = CartItemsListAdapter(this@CheckoutActivity, mCartItemsList, false)
        binding.rvCartListItems.adapter = cartListAdapter

        for (item in mCartItemsList) {

            val availableQuantity = item.stock_quantity.toInt()

            if (availableQuantity > 0) {
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()

                mSubTotal += (price * quantity)
            }
        }

        binding.tvCheckoutSubTotal.text = "₹ $mSubTotal"
        // Here we have kept Shipping Charge is fixed as $10 but in your case it may cary. Also, it depends on the location and total amount.
        binding.tvCheckoutShippingCharge.text = "₹ 40.0"

        if (mSubTotal > 0) {
            binding.llCheckoutPlaceOrder.visibility = View.VISIBLE

            mTotalAmount = mSubTotal + 10.0
            binding.tvCheckoutTotalAmount.text = "₹ $mTotalAmount"
        } else {
            binding.llCheckoutPlaceOrder.visibility = View.GONE
        }

    }
    private fun placeAnOrder() {

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        // TODO Step 7: Make the variable global.
        mOrderDetails = Order(
            FirestoreClass().getCurrentUserID(),
            mCartItemsList,
            mAddressDetails!!,
            "My order ${System.currentTimeMillis()}",
            mCartItemsList[0].image,
            mSubTotal.toString(),
            "10.0", // The Shipping Charge is fixed as $10 for now in our case.
            mTotalAmount.toString(),
            System.currentTimeMillis()
        )

        FirestoreClass().placeOrder(this@CheckoutActivity, mOrderDetails)
    }
    // END


    // TODO Step 8: Create a function to notify the success result of the order placed.
    // START
    /**
     * A function to notify the success result of the order placed.
     */
    fun orderPlacedSuccess() {

       FirestoreClass().updateAllDetails(this,mCartItemsList,mOrderDetails)
    }
    // END

    fun allDetailsUpdatedSuccessfully() {

        // TODO Step 6: Move the piece of code from OrderPlaceSuccess to here.
        // START
        // Hide the progress dialog.
        hideProgressDialog()

        Toast.makeText(this@CheckoutActivity, "Your order placed successfully.", Toast.LENGTH_SHORT)
            .show()

        val intent = Intent(this@CheckoutActivity, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
        // END
    }
}