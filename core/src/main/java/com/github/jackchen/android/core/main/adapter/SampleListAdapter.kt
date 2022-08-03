package com.github.jackchen.android.core.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.jackchen.android.core.AndroidSample
import com.github.jackchen.android.core.databinding.SampleListItemBinding
import com.github.jackchen.android.core.util.displayDesc
import com.github.jackchen.android.core.util.displayTitle
import com.github.jackchen.android.sample.api.SampleItem

/**
 * @author Created by cz
 * @date 2020-01-27 21:42
 * @email bingo110@126.com
 */
class SampleListAdapter(items: MutableList<AndroidSample.PathNode>) :
  MutableListAdapter<AndroidSample.PathNode, SampleListAdapter.ViewHolder>(items) {

  override fun compareItem(
    checkContent: Boolean,
    first: AndroidSample.PathNode,
    second: AndroidSample.PathNode
  ): Boolean {
    return if (checkContent) first == second else first === second
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = SampleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(this[position])
    holder.itemView.setOnClickListener { v -> dispatchItemClickEvent(v, holder.layoutPosition) }
  }

  class ViewHolder(private val binding: SampleListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(node: AndroidSample.PathNode) {
      binding.sampleTitle.text = node.displayTitle()
      val desc = node.displayDesc()
      if (desc.isNotEmpty()) {
        binding.sampleDescription.text = desc
        binding.sampleDescription.visibility = View.VISIBLE
      } else {
        binding.sampleDescription.visibility = View.GONE
      }

      val item = node.item
      if (item is String) {
        binding.sampleImageArrow.visibility = View.VISIBLE
      } else if (item is SampleItem) {
        binding.sampleImageArrow.visibility = View.GONE
      }
    }
  }
}
