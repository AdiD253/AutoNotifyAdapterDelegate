package com.adid.autonotifyadapterdelegate

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adid.adapterdelegate.autoNotifyDelegate
import com.adid.autonotifyadapterdelegate.model.SampleItem
import kotlinx.android.synthetic.main.item_sample_adapter.view.*

class SampleAdapter : RecyclerView.Adapter<SampleViewHolder>() {

    private var itemList: List<SampleItem> by autoNotifyDelegate(
        adapter = this,
        initialValue = listOf(),
        async = true
    )

    fun setItems(itemList: List<SampleItem>) {
        this.itemList = itemList
    }

    private lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleViewHolder {
        if (!this::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        val view = inflater.inflate(R.layout.item_sample_adapter, parent, false)
        return SampleViewHolder(view)
    }

    override fun onBindViewHolder(holder: SampleViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount() = itemList.size
}

class SampleViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: SampleItem) {
        view.sampleItem.text = item.name
        view.setBackgroundColor(ContextCompat.getColor(view.context, item.color))
    }
}