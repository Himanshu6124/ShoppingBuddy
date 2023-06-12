package com.example.shopify.ui.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.Firestore.FirestoreClass
import com.example.shopify.R
import com.example.shopify.adapter.AddressListAdapter
import com.example.shopify.databinding.ActivityAddressListBinding
import com.example.shopify.models.Address
import com.example.shopify.utils.Constants
import com.example.shopify.utils.SwipeToDeleteCallback
import com.example.shopify.utils.SwipeToEditCallback

class AddressListActivity : BaseActivity() {
    private var mSelectAddress: Boolean = false
    private lateinit var binding : ActivityAddressListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_address_list)

        if (intent.hasExtra(Constants.EXTRA_SELECT_ADDRESS)) {
            mSelectAddress =
                intent.getBooleanExtra(Constants.EXTRA_SELECT_ADDRESS, false)
        }

        setupActionBar()
        getAddressList()

        if (mSelectAddress) {
            binding.tvTitle.text = resources.getString(R.string.title_select_address)
        }

        binding.tvAddAddress.setOnClickListener{
            val intent = Intent(this,AddEditAddressActivity::class.java)
            Log.d("fsfs","ssfs")
            startActivityForResult(intent,Constants.ADD_ADDRESS_REQUEST_CODE)
        }
    }

//    override fun onResume() {
//        super.onResume()
//        getAddressList()
//
//    }

    @Deprecated("Deprecated in Java")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.ADD_ADDRESS_REQUEST_CODE) {

                getAddressList()
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "To add the address.")
        }
    }

    private fun setupActionBar() {


        setSupportActionBar(binding.toolbarAddressListActivity)

        val actionBar = supportActionBar

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }

        binding.toolbarAddressListActivity.setNavigationOnClickListener { onBackPressed() }

    }
    fun successAddressListFromFirestore(addressList: ArrayList<Address>) {

        // Hide the progress dialog
        hideProgressDialog()


        // Print all the list of addresses in the log with name.
//        for (i in addressList) {
//
//            Log.i("Name and Address", "${i.name} ::  ${i.address}")
//        }

        if (addressList.size > 0) {

            binding.rvAddressList.visibility = View.VISIBLE
            binding.tvNoAddressFound.visibility = View.GONE

            binding.rvAddressList.layoutManager = LinearLayoutManager(this@AddressListActivity)
            binding.rvAddressList.setHasFixedSize(true)

            val addressAdapter = AddressListAdapter(this@AddressListActivity, addressList,mSelectAddress)
            binding.rvAddressList.adapter = addressAdapter

            if (!mSelectAddress) {
                val editSwipeHandler = object : SwipeToEditCallback(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                        val adapter = binding.rvAddressList.adapter as AddressListAdapter
                        adapter.notifyEditItem(
                            this@AddressListActivity,
                            viewHolder.adapterPosition
                        )
                    }
                }

                val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
                editItemTouchHelper.attachToRecyclerView(binding.rvAddressList)


                val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                        // Show the progress dialog.
                        showProgressDialog(resources.getString(R.string.please_wait))

                        FirestoreClass().deleteAddress(
                            this@AddressListActivity,
                            addressList[viewHolder.adapterPosition].id
                        )
                    }
                }
                val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
                deleteItemTouchHelper.attachToRecyclerView(binding.rvAddressList)
            }


        } else
        {
            binding.rvAddressList.visibility = View.GONE
            binding.tvNoAddressFound.visibility = View.VISIBLE
        }


    }
//    * A function to get the list of address from cloud firestore.
//    */
    private fun getAddressList() {

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getAddressesList(this@AddressListActivity)
    }
    fun deleteAddressSuccess() {

        // Hide progress dialog.
        hideProgressDialog()

        Toast.makeText(
            this@AddressListActivity,
            resources.getString(R.string.err_your_address_deleted_successfully),
            Toast.LENGTH_SHORT
        ).show()

        getAddressList()
    }

}