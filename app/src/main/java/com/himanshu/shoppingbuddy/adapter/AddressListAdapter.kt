package com.himanshu.shoppingbuddy.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.himanshu.shoppingbuddy.R
import com.himanshu.shoppingbuddy.databinding.ItemAddressLayoutBinding
import com.himanshu.shoppingbuddy.models.Address
import com.himanshu.shoppingbuddy.ui.activities.AddEditAddressActivity
import com.himanshu.shoppingbuddy.ui.activities.CheckoutActivity
import com.himanshu.shoppingbuddy.utils.Constants

class AddressListAdapter(private val context: Context,
                         private var list: ArrayList<Address>, private val selectAddress : Boolean) :

    RecyclerView.Adapter<AddressListAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        var binding = ItemAddressLayoutBinding.bind(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
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
                val intent =Intent(context, CheckoutActivity::class.java)
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
        activity.startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)

        notifyItemChanged(position) // Notify any registered observers that the item at position has changed.
    }
}