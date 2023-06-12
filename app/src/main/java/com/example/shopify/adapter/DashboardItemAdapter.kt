package com.example.shopify.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.R
import com.example.shopify.databinding.ItemDashboardLayoutBinding
import com.example.shopify.databinding.ItemListLayoutBinding
import com.example.shopify.models.Product
import com.example.shopify.ui.activities.ProductDetailsActivity
import com.example.shopify.ui.fragments.DashboardFragment
import com.example.shopify.ui.fragments.ProductsFragment
import com.example.shopify.utils.Constants
import com.example.shopify.utils.GlideLoader

open class DashboardItemAdapter(
    private val context: Context,
    private var list: ArrayList<Product>,
    private val fragment: DashboardFragment
) : RecyclerView.Adapter<DashboardItemAdapter.MyViewHolder>() {

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_dashboard_layout,
                parent,
                false
            )
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

//        if (holder is MyViewHolder) {

        GlideLoader(context).loadProductPicture(model.image, holder.binding.ivDashboardItemImage)

        holder.binding.tvDashboardItemPrice.text = model.title
        holder.binding.tvDashboardItemPrice.text = "₹ ${model.price}"

        holder.itemView.setOnClickListener{

            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_PRODUCT_ID,model.product_id)

            // to check if owner has uploaded product and dont show add to cart
            intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID,model.user_id)

            context.startActivity(intent)
        }


//        holder.binding.ib
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        var binding = ItemDashboardLayoutBinding.bind(view)

    }

}