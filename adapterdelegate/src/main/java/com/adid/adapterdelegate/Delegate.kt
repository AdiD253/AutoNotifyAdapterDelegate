package com.adid.adapterdelegate

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface DiffItem {

    fun getItemId(): String
    fun getDiff(): String
}

fun <T : List<DiffItem>, R : RecyclerView.ViewHolder> autoNotifyDelegate(adapter: RecyclerView.Adapter<R>, initialValue: T): ReadWriteProperty<Any?, T> =
    object : ObservableProperty<T>(initialValue) {
        override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
            adapter.autoNotify(oldValue, newValue)
        }
    }

fun <T : DiffItem, R: RecyclerView.ViewHolder> RecyclerView.Adapter<R>.autoNotify(
    oldList: List<T>, newList: List<T>) {

    val diffItemCallback = object : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].getItemId() == newList[newItemPosition].getItemId()

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].getDiff() == newList[newItemPosition].getDiff()

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size
    }

    DiffUtil.calculateDiff(diffItemCallback).dispatchUpdatesTo(this)
}