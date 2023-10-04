package com.himanshu.shoppingbuddy.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.himanshu.shoppingbuddy.R
import com.himanshu.shoppingbuddy.adapter.CartItemsListAdapter
import com.himanshu.shoppingbuddy.databinding.ActivityCartListBinding
import com.himanshu.shoppingbuddy.models.CartItems
import com.himanshu.shoppingbuddy.models.Product
import com.himanshu.shoppingbuddy.utils.Constants

class CartListActivity : BaseActivity() {
    private lateinit var mProductsList : ArrayList<Product>
    private lateinit var mCartListItem : ArrayList<CartItems>
    private lateinit var binding : ActivityCartListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

    }
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarCartListActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }

        binding.btnCheckout.setOnClickListener {
            val intent = Intent(this@CartListActivity, AddressListActivity::class.java)
            intent.putExtra(Constants.EXTRA_SELECT_ADDRESS, true)
            startActivity(intent)
        }



        binding.toolbarCartListActivity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onResume() {
        super.onResume()

        getProductList()
    }


    private fun getCartItemsList() {

        // Show the progress dialog.
//        showProgressDialog(resources.getString(R.string.please_wait))

        com.himanshu.shoppingbuddy.Firestore.FirestoreClass().getCartList(this@CartListActivity)
    }

    /**
     * A function to notify the success result of the cart items list from cloud firestore.
     *
     * @param cartList
     */
    fun successCartItemsList(cartList: ArrayList<CartItems>) {

        // Hide progress dialog.
        hideProgressDialog()

        // START

        for (product in mProductsList) {
            for (cart in cartList) {
                if (product.product_id == cart.product_id) {

                    cart.stock_quantity = product.stock_quantity

                    if (product.stock_quantity.toInt() == 0){
                        cart.cart_quantity = product.stock_quantity
                    }
                }
            }
        }
        // END

        // START
        mCartListItem = cartList


        /*for (i in cartList) {

            Log.i("Cart Item Title", i.title)

        }*/

        if (mCartListItem.size > 0) {

            binding.rvCartItemsList.visibility = View.VISIBLE
            binding.llCheckout.visibility = View.VISIBLE
            binding.tvNoCartItemFound.visibility = View.GONE

            binding.rvCartItemsList.layoutManager = LinearLayoutManager(this@CartListActivity)
            binding.rvCartItemsList.setHasFixedSize(true)

            val cartListAdapter = CartItemsListAdapter(this@CartListActivity, mCartListItem,true)
            binding.rvCartItemsList.adapter = cartListAdapter

            var subTotal: Double = 0.0

            for (item in mCartListItem) {

                val availableQuantity = item.stock_quantity.toInt()

                if (availableQuantity > 0) {
                    val price = item.price.toDouble()
                    val quantity = item.cart_quantity.toInt()

                    subTotal += (price * quantity)
                }
            }

            binding.tvSubTotal.text = "₹ $subTotal"
            // Here we have kept Shipping Charge is fixed as $10 but in your case it may cary. Also, it depends on the location and total amount.
            binding.tvShippingCharge.text = "₹40.0"

            if (subTotal > 0) {
                binding.llCheckout.visibility = View.VISIBLE

                val total = subTotal + 10
                binding.tvTotalAmount.text = "₹ $total"
            } else {
                binding.llCheckout.visibility = View.GONE
            }

        } else {
            binding.rvCartItemsList.visibility = View.GONE
            binding.llCheckout.visibility = View.GONE
            binding.tvNoCartItemFound.visibility = View.VISIBLE
        }
    }

    private fun getProductList() {

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        com.himanshu.shoppingbuddy.Firestore.FirestoreClass().getAllProductsList(this@CartListActivity)
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        // TODO Step 7: Initialize the product list global variable once we have the product list.
        // START
        mProductsList = productsList
        // END

        // TODO Step 8: Once we have the latest product list from cloud firestore get the cart items list from cloud firestore.
        // START
        getCartItemsList()
        // END
    }

    fun itemRemovedSuccess() {

        hideProgressDialog()

        Toast.makeText(
            this@CartListActivity,
            resources.getString(R.string.msg_item_removed_successfully),
            Toast.LENGTH_SHORT
        ).show()

        getCartItemsList()
    }

    fun itemUpdateSuccess() {

        hideProgressDialog()

        getCartItemsList()
    }
}