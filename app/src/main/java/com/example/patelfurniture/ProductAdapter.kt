package com.example.patelfurniture

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private var productList: List<Product>,
    private val onAddToCart: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productCategory: TextView = itemView.findViewById(R.id.productCategory)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val productStock: TextView = itemView.findViewById(R.id.productStock)
        val addToCartButton: Button = itemView.findViewById(R.id.addToCartButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.productName.text = product.name
        holder.productCategory.text = product.category
        holder.productPrice.text = "$${"%.2f".format(product.price)}"
        holder.productStock.text = "${product.stock} in stock"

        holder.addToCartButton.setOnClickListener {
            onAddToCart(product)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun updateProductList(newList: List<Product>) {
        productList = newList
        notifyDataSetChanged()
    }
}
