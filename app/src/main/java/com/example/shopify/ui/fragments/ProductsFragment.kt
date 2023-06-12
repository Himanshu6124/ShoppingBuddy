package com.example.shopify.ui.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.Firestore.FirestoreClass
import com.example.shopify.R
import com.example.shopify.adapter.MyProductsListAdapter
import com.example.shopify.databinding.FragmentProductsBinding
import com.example.shopify.models.Product
import com.example.shopify.ui.activities.AddProductActivity

class ProductsFragment : BaseFragment() {

    private lateinit var binding: FragmentProductsBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setHasOptionsMenu(true)







        return root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.add_product_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId)
        {
            R.id.action_add_product->{
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }

        }

        return super.onOptionsItemSelected(item)
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {
        hideProgressDialog()

        Log.i("HPHP" ,productsList.size.toString())

        if(productsList.size > 0)
        {
            binding.rvMyProductItems.visibility = View.VISIBLE
            binding.tvNoProductsFound.visibility =View.GONE
            binding.rvMyProductItems.layoutManager =LinearLayoutManager(activity)
            binding.rvMyProductItems.setHasFixedSize(true)

            val adapter = MyProductsListAdapter(requireContext(),productsList,this)
            binding.rvMyProductItems.adapter = adapter

        }

//        Log.i("HP",productsList[1].description)

    }
    private fun getProductListFromFireStore()
    {
        showProgressDialog("Please Wait")
        FirestoreClass().getProductsList(this)

    }

    fun deleteProduct(productID: String) {

        // TODO Step 6: Remove the toast message and call the function to ask for confirmation to delete the product.
        // START
        // Here we will call the delete function of the FirestoreClass. But, for now lets display the Toast message and call this function from adapter class.

        /*Toast.makeText(
            requireActivity(),
            "You can now delete the product. $productID",
            Toast.LENGTH_SHORT
        ).show()*/

        showAlertDialogToDeleteProduct(productID)
        // END
    }

    // TODO Step 2: Create a function to notify the success result of product deleted from cloud firestore.
    // START
    /**
     * A function to notify the success result of product deleted from cloud firestore.
     */
    fun productDeleteSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            requireActivity(),
            resources.getString(R.string.product_delete_success_message),
            Toast.LENGTH_SHORT
        ).show()

        // Get the latest products list from cloud firestore.
        getProductListFromFireStore()
    }
    // END

    // TODO Step 5: Create a function to show the alert dialog for the confirmation of delete product from cloud firestore.
    // START
    /**
     * A function to show the alert dialog for the confirmation of delete product from cloud firestore.
     */
    private fun showAlertDialogToDeleteProduct(productID: String) {

        val builder = AlertDialog.Builder(requireActivity())
        //set title for alert dialog
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        //set message for alert dialog
        builder.setMessage(resources.getString(R.string.delete_dialog_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->

            // TODO Step 7: Call the function to delete the product from cloud firestore.
            // START
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Call the function of Firestore class.
            FirestoreClass().deleteProduct(this@ProductsFragment, productID)
            // END

            dialogInterface.dismiss()
        }

        //performing negative action
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->

            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }


    override fun onResume() {
        super.onResume()
        getProductListFromFireStore()
    }


}