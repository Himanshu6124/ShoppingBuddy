package com.himanshu.shoppingbuddy.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.himanshu.shoppingbuddy.Firestore.FirestoreClass
import com.himanshu.shoppingbuddy.R
import com.himanshu.shoppingbuddy.adapter.SoldProductsListAdapter
import com.himanshu.shoppingbuddy.databinding.FragmentOrdersBinding
import com.himanshu.shoppingbuddy.databinding.FragmentSoldProductsBinding
import com.himanshu.shoppingbuddy.models.SoldProduct

class SoldProductsFragment : BaseFragment() {
    private lateinit var binding : FragmentSoldProductsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSoldProductsBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onResume() {
    super.onResume()

    getSoldProductsList()
}

// TODO Step 4: Create a function to get the list of sold products.
// START
private fun getSoldProductsList() {
    // Show the progress dialog.
    showProgressDialog(resources.getString(R.string.please_wait))

    // Call the function of Firestore class.
    com.himanshu.shoppingbuddy.Firestore.FirestoreClass().getSoldProductsList(this@SoldProductsFragment)
}
// END

// TODO Step 2: Create a function to get the success result list of sold products.
// START
/**
 * A function to get the list of sold products.
 */
fun successSoldProductsList(soldProductsList: ArrayList<SoldProduct>) {

    // Hide Progress dialog.
    hideProgressDialog()

    // TODO Step 7: Populate the list in the RecyclerView using the adapter class.
    // START
    if (soldProductsList.size > 0) {
        binding.rvSoldProductItems.visibility = View.VISIBLE
        binding.tvNoSoldProductsFound.visibility = View.GONE

        binding.rvSoldProductItems.layoutManager = LinearLayoutManager(activity)
        binding.rvSoldProductItems.setHasFixedSize(true)

        val soldProductsListAdapter =
            SoldProductsListAdapter(requireActivity(), soldProductsList)
        binding.rvSoldProductItems.adapter = soldProductsListAdapter
    } else {
        binding.rvSoldProductItems.visibility = View.GONE
        binding.tvNoSoldProductsFound.visibility = View.VISIBLE
    }
    // END
}

}