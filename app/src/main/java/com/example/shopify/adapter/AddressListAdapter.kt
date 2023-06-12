package com.example.shopify.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.R
import com.example.shopify.databinding.ItemAddressLayoutBinding
import com.example.shopify.databinding.ItemCartLayoutBinding
import com.example.shopify.models.Address
import com.example.shopify.models.CartItems
import com.example.shopify.ui.activities.AddEditAddressActivity
import com.example.shopify.ui.activities.CheckoutActivity
import com.example.shopify.utils.Constants

class AddressListAdapter(private val context: Context,
                         private var list: ArrayList<Address>, private val selectAddress : Boolean) :

    RecyclerView.Adapter<AddressListAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        var binding = ItemAddressLayoutBinding.bind(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return AddressListAdapter.MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_address_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        holder.binding.tvAddressFullName.text = model.name
        holder.binding.tvAddressType.text = model.type
        holder.binding.tvAddressDetails.text =  "${model.address}, ${model.zipCode}"
        holder.binding.tvAddressMobileNumber.text = model.mobileNumber

        if (selectAddress) {
            holder.itemView.setOnClickListener {
                val intent =Intent(context,CheckoutActivity::class.java)
                intent.putExtra(Constants.EXTRA_SELECTED_ADDRESS, model)
                context.startActivity(intent)
            }
        }

    }
    fun notifyEditItem(activity: Activity, position: Int) {
        val intent = Intent(context, AddEditAddressActivity::class.java)
        // TODO Step 6: Pass the address details through intent to edit the address.
        // START
        intent.putExtra(Constants.EXTRA_ADDRESS_DETAILS, list[position])
        // END
        activity.startActivityForResult(intent,Constants.ADD_ADDRESS_REQUEST_CODE)

        notifyItemChanged(position) // Notify any registered observers that the item at position has changed.
    }
}