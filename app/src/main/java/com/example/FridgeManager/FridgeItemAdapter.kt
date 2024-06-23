package com.example.FridgeManager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.FridgeManager.databinding.ItemViewBinding

class FridgeItemAdapter(
    private val items: List<FridgeItem>,
    private val onItemClick: (FridgeItem) -> Unit
    ) : RecyclerView.Adapter<FridgeItemAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemViewBinding)
        : RecyclerView.ViewHolder(binding.root) {
            fun bind(item: FridgeItem) {
                binding.itemImageView.setImageResource(item.imageResId)
                binding.itemNameTextView.text = "商品名: ${item.name}"
                binding.itemNumberTextView.text = "数量: ${item.number}"
                binding.itemExpiryDateView.text = "消費期限: ${item.expiryDate}"
                binding.root.setOnClickListener {
                    onItemClick(item)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemViewBinding.
                inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
