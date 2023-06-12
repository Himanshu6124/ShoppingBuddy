package com.example.shopify.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.R
import com.example.shopify.databinding.ItemListLayoutBinding
import com.example.shopify.models.Product
import com.example.shopify.ui.activities.ProductDetailsActivity
import com.example.shopify.ui.fragments.ProductsFragment
import com.example.shopify.utils.Constants
import com.example.shopify.utils.GlideLoader

open class MyProductsListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>,
    private val fragment: ProductsFragment
) : RecyclerView.Adapter<MyProductsListAdapter.MyViewHolder>() {

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_layout,
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

        GlideLoader(context).loadProductPicture(model.image, holder.binding.ivItemImage)

        holder.binding.tvItemName.text = model.title
        holder.binding.tvItemPrice.text = "$${model.price}"

        holder.binding.ibDeleteProduct.setOnClickListener {

            fragment.deleteProduct(model.product_id)
        }

        holder.itemView.setOnClickListener{

            val intent = Intent(context,ProductDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_PRODUCT_ID,model.product_id)
            // to check if owner has uploaded product and dont show add to cart
            intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID,model.user_id)

            context.startActivity(intent)
        }

    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        var binding = ItemListLayoutBinding.bind(view)

    }

}