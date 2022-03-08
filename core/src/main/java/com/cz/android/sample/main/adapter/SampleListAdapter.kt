package com.cz.android.sample.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cz.android.sample.api.SampleItem
import com.cz.android.sample.core.databinding.SampleListItemBinding

/**
 * @author Created by cz
 * @date 2020-01-27 21:42
 * @email bingo110@126.com
 */
class SampleListAdapter(items: MutableList<SampleItem>) :
    MutableListAdapter<SampleItem, SampleListAdapter.ViewHolder>(items) {

    override fun compareItem(checkContent: Boolean, first: SampleItem, second: SampleItem): Boolean {
        return if (checkContent) first == second else first === second
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SampleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(this[position], position)
    }

    class ViewHolder(private val binding: SampleListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(sampleItem: SampleItem, position: Int) {
            binding.textTitle.text = sampleItem.title
        }
    }
}