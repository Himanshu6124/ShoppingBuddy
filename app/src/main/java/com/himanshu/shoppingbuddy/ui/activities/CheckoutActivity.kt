package com.himanshu.shoppingbuddy.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.himanshu.shoppingbuddy.Firestore.FirestoreClass
import com.himanshu.shoppingbuddy.R
import com.himanshu.shoppingbuddy.adapter.CartItemsListAdapter
import com.himanshu.shoppingbuddy.databinding.ActivityCheckoutBinding
import com.himanshu.shoppingbuddy.models.Address
import com.himanshu.shoppingbuddy.models.CartItems
import com.himanshu.shoppingbuddy.models.Order
import com.himanshu.shoppingbuddy.models.Product
import com.himanshu.shoppingbuddy.utils.Constants

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
    private val paymentMethodList : ArrayList<String> = arrayListOf("COD","UPI")
    private var selectedPayment : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_checkout)
        setupActionBar()

        binding.tvPaymentMode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, p1: View?, p2: Int, p3: Long) {

                val selectedPosition = parent.getItemAtPosition(p2).toString()
                selectedPayment = selectedPosition

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }


        val paymentMethodAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,paymentMethodList)
        binding.tvPaymentMode.adapter = paymentMethodAdapter


        if (intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)) {
            mAddressDetails =
                intent.getParcelableExtra(Constants.EXTRA_SELECTED_ADDRESS)!!

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

        mProductsList = productsList
        getCartItemsList()
    }
    /**
     * A function to get the list of cart items in the activity.
     */
    private fun getCartItemsList() {

        FirestoreClass().getCartList(this@CheckoutActivity)
    }

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

        if(selectedPayment == "UPI"){
            val intent = Intent(this,PaymentActivity::class.java)
            startActivity(intent)
        }

        else {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

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

            FirestoreClass()
                .placeOrder(this@CheckoutActivity, mOrderDetails)
        }
    }


    /**
     * A function to notify the success result of the order placed.
     */
    fun orderPlacedSuccess() {

       FirestoreClass()
           .updateAllDetails(this,mCartItemsList,mOrderDetails)
    }
    // END

    fun allDetailsUpdatedSuccessfully() {

        hideProgressDialog()

        Toast.makeText(this@CheckoutActivity, "Your order placed successfully.", Toast.LENGTH_SHORT)
            .show()

        val intent = Intent(this@CheckoutActivity, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}