package com.example.nutanindustries1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private val cartList: MutableList<CartItem>,
    private val onIncreaseQuantity: (CartItem) -> Unit,
    private val onDelete: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val tvTotalPrice: TextView = itemView.findViewById(R.id.tvTotalPrice)
        val btnIncrease: Button = itemView.findViewById(R.id.btnIncreaseQuantity)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartList[position]
        holder.tvProductName.text = cartItem.product.name
        holder.tvQuantity.text = "Quantity: ${cartItem.quantity}"
        holder.tvTotalPrice.text = "Total: ₹${cartItem.totalPrice}"

        holder.btnIncrease.setOnClickListener { onIncreaseQuantity(cartItem) }
        holder.btnDelete.setOnClickListener { onDelete(cartItem) }
    }

    override fun getItemCount(): Int = cartList.size
}
