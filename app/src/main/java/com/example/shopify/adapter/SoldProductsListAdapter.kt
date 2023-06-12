package com.example.shopify.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.R
import com.example.shopify.databinding.ItemListLayoutBinding
import com.example.shopify.models.SoldProduct
import com.example.shopify.ui.activities.MyOrderDetailsActivity
import com.example.shopify.ui.activities.SoldProductDetailsActivity
import com.example.shopify.utils.Constants
import com.example.shopify.utils.GlideLoader

open class SoldProductsListAdapter(
    private val context: Context,
    private var list: ArrayList<SoldProduct>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var binding = ItemListLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlideLoader(context).loadProductPicture(
                model.image,
                holder.binding.ivItemImage
            )

            holder.binding.tvItemName.text = model.title
            holder.binding.tvItemPrice.text = "$${model.price}"
            holder.binding.ibDeleteProduct.visibility = View.GONE

            holder.itemView.setOnClickListener {
                val intent = Intent(context, SoldProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS, model)
                context.startActivity(intent)
            }
        }
    }


}