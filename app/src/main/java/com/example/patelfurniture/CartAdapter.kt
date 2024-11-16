package com.example.patelfurniture

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(private val cartItems: List<CartItem>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.productName.text = cartItem.product.name
        holder.productPrice.text = "₹${"%.2f".format(cartItem.product.price)}"
        holder.productQuantity.text = "Quantity: ${cartItem.quantity}"
        holder.totalPrice.text = "Total: ₹${"%.2f".format(cartItem.totalPrice)}"
    }

    override fun getItemCount(): Int = cartItems.size

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.tv_product_name)
        val productPrice: TextView = itemView.findViewById(R.id.tv_product_price)
        val productQuantity: TextView = itemView.findViewById(R.id.tv_product_quantity)
        val totalPrice: TextView = itemView.findViewById(R.id.tv_total_price)
    }
}
