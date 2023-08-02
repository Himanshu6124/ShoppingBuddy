package com.himanshu.shoppingbuddy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.himanshu.shoppingbuddy.Firestore.FirestoreClass
import com.himanshu.shoppingbuddy.R
import com.himanshu.shoppingbuddy.databinding.ItemCartLayoutBinding
import com.himanshu.shoppingbuddy.databinding.ItemDashboardLayoutBinding
import com.himanshu.shoppingbuddy.models.CartItems
import com.himanshu.shoppingbuddy.models.Product
import com.himanshu.shoppingbuddy.ui.activities.CartListActivity
import com.himanshu.shoppingbuddy.utils.Constants
import com.himanshu.shoppingbuddy.utils.GlideLoader

class CartItemsListAdapter(private val context: Context,
                           private var list: ArrayList<CartItems>,
                           private val updateCartItems: Boolean) :
    RecyclerView.Adapter<CartItemsListAdapter.MyViewHolder>() {


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        var binding =ItemCartLayoutBinding.bind(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_cart_layout,
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

        GlideLoader(context).loadProductPicture(model.image, holder.binding.ivCartItemImage)

        holder.binding.tvCartItemTitle.text = model.title
        holder.binding.tvCartItemPrice.text = "₹ ${model.price}"
        holder.binding.tvCartQuantity.text = model.cart_quantity

        if (model.cart_quantity == "0") {
            holder.binding.ibRemoveCartItem.visibility = View.GONE
            holder.binding.ibAddCartItem.visibility = View.GONE

            if (updateCartItems) {
                holder.binding.ibDeleteCartItem.visibility = View.VISIBLE
            } else {
                holder.binding.ibDeleteCartItem.visibility = View.GONE
            }

            holder.binding.tvCartQuantity.text =
                context.resources.getString(R.string.lbl_out_of_stock)

            holder.binding.tvCartQuantity.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorSnackBarError
                )
            )
        } else {
//            holder.binding.ibRemoveCartItem.visibility = View.VISIBLE
//            holder.binding.ibAddCartItem.visibility = View.VISIBLE

            if (updateCartItems) {
                holder.binding.ibRemoveCartItem.visibility = View.VISIBLE
                holder.binding.ibAddCartItem.visibility = View.VISIBLE
                holder.binding.ibDeleteCartItem.visibility = View.VISIBLE
            } else {

                holder.binding.ibRemoveCartItem.visibility = View.GONE
                holder.binding.ibAddCartItem.visibility = View.GONE
                holder.binding.ibDeleteCartItem.visibility = View.GONE
            }

            holder.binding.tvCartQuantity.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorSecondaryText
                )
            )
        }

        holder.binding.ibRemoveCartItem.setOnClickListener {

            // TODO Step 6: Call the update or remove function of firestore class based on the cart quantity.
            // START
            if (model.cart_quantity == "1") {
                com.himanshu.shoppingbuddy.Firestore.FirestoreClass()
                    .removeItemFromCart(context, model.id)
            } else {

                val cartQuantity: Int = model.cart_quantity.toInt()

                val itemHashMap = HashMap<String, Any>()

                itemHashMap[Constants.CART_QUANTITY] = (cartQuantity - 1).toString()

                // Show the progress dialog.

                if (context is CartListActivity) {
                    context.showProgressDialog(context.resources.getString(R.string.please_wait))
                }

                com.himanshu.shoppingbuddy.Firestore.FirestoreClass()
                    .updateMyCart(context, model.id, itemHashMap)
            }
            // END
        }

        holder.binding.ibAddCartItem.setOnClickListener {

            // TODO Step 8: Call the update function of firestore class based on the cart quantity.
            // START
            val cartQuantity: Int = model.cart_quantity.toInt()

            if (cartQuantity < model.stock_quantity.toInt()) {

                val itemHashMap = HashMap<String, Any>()

                itemHashMap[Constants.CART_QUANTITY] = (cartQuantity + 1).toString()

                // Show the progress dialog.
                if (context is CartListActivity) {
                    context.showProgressDialog(context.resources.getString(R.string.please_wait))
                }

                com.himanshu.shoppingbuddy.Firestore.FirestoreClass()
                    .updateMyCart(context, model.id, itemHashMap)
            } else {
                if (context is CartListActivity) {
                    context.showErrorSnackBar(
                        context.resources.getString(
                            R.string.msg_for_available_stock,
                            model.stock_quantity
                        ),
                        true
                    )
                }
            }
            // END
        }
        // END



        holder.binding.ibDeleteCartItem.setOnClickListener {



            when (context) {
                is CartListActivity -> {
                    context.showProgressDialog(context.resources.getString(R.string.please_wait))
                }
            }

            com.himanshu.shoppingbuddy.Firestore.FirestoreClass().removeItemFromCart(context, model.id)
            // END
        }

    }
}