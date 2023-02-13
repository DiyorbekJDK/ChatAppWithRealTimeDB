package com.sdk.realtimedatabase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sdk.realtimedatabase.databinding.ProductLayoutBinding
import com.sdk.realtimedatabase.model.Product

class ProductAdapter(
    private val itemClickHandler: ItemClickHandler
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(DiffCallBack()) {
    private class DiffCallBack : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ProductLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(private val binding: ProductLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                tvName.text = product.name
                tvPrice.text = product.price.toString()
                btnDelete.setOnClickListener {
                    itemClickHandler.onDelete(product, adapterPosition)
                }
                btnEdit.setOnClickListener {
                    itemClickHandler.onUpdate(product)
                }
            }
        }
    }
}