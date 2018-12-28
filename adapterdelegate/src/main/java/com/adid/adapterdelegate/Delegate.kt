package com.adid.adapterdelegate

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface DiffItem {

    /** ItemId which defines unique element **/
    fun getItemId(): String

    /** Value which defines difference between items with the same Id. **/
    fun getDiff(): String
}

fun <T : List<DiffItem>, R : RecyclerView.ViewHolder> autoNotifyDelegate(
    adapter: RecyclerView.Adapter<R>,
    initialValue: T
): ReadWriteProperty<Any?, T> =
    object : ObservableProperty<T>(initialValue) {
        override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
            adapter.autoNotify(oldValue, newValue)
        }
    }

private fun <T : DiffItem, R: RecyclerView.ViewHolder> RecyclerView.Adapter<R>.autoNotify(
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