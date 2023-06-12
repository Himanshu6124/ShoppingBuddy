package com.example.shopify.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.Firestore.FirestoreClass
import com.example.shopify.R
import com.example.shopify.adapter.MyOrdersListAdapter
import com.example.shopify.databinding.FragmentOrdersBinding
import com.example.shopify.models.Order

class OrdersFragment : BaseFragment() {

    private lateinit var binding: FragmentOrdersBinding

    // This property is only valid between onCreateView and
    // onDestroyView.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentOrdersBinding.inflate(inflater, container, false)



        return binding.root
    }


    /**
     * A function to get the success result of the my order list from cloud firestore.
     *
     * @param ordersList List of my orders.
     */
    fun populateOrdersListInUI(ordersList: ArrayList<Order>) {

        // Hide the progress dialog.
        hideProgressDialog()

        // TODO Step 11: Populate the orders list in the UI.
        // START
        if (ordersList.size > 0) {

            binding.rvMyOrderItems.visibility = View.VISIBLE
            binding.tvNoOrdersFound.visibility = View.GONE

            binding.rvMyOrderItems.layoutManager = LinearLayoutManager(activity)
            binding.rvMyOrderItems.setHasFixedSize(true)

            val myOrdersAdapter = MyOrdersListAdapter(requireActivity(), ordersList)
            binding.rvMyOrderItems.adapter = myOrdersAdapter
        } else {
            binding.rvMyOrderItems.visibility = View.GONE
            binding.tvNoOrdersFound.visibility = View.VISIBLE
        }
        // END
    }
    private fun getMyOrdersList() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getMyOrdersList(this@OrdersFragment)
    }

    override fun onResume() {
        super.onResume()
        getMyOrdersList()
    }
}