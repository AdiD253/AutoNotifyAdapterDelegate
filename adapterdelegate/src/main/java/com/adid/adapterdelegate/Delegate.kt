package com.adid.adapterdelegate

import android.support.v7.recyclerview.extensions.AsyncListDiffer
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
    initialValue: T,
    async: Boolean = false
): ReadWriteProperty<Any?, T> =
    object : ObservableProperty<T>(initialValue) {
        override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
            if (async) {
                adapter.asyncAutoNotify(newValue)
            } else {
                adapter.autoNotify(oldValue, newValue)
            }
        }
    }

private fun <T : RecyclerView.ViewHolder> RecyclerView.Adapter<T>.autoNotify(
    oldList: List<DiffItem>, newList: List<DiffItem>
) {

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

private fun <T : RecyclerView.ViewHolder> RecyclerView.Adapter<T>.asyncAutoNotify(
    newList: List<DiffItem>
) {
    if (!::asyncListDiffer.isInitialized) {
        asyncListDiffer = AsyncListDiffer(this, diffItemCallback)
    }

    asyncListDiffer.submitList(newList)
}

private val diffItemCallback = object : DiffUtil.ItemCallback<DiffItem>() {
    override fun areItemsTheSame(p0: DiffItem, p1: DiffItem) =
        p0.getItemId() == p1.getItemId()



    override fun areContentsTheSame(p0: DiffItem, p1: DiffItem) =
        p0.getDiff() == p1.getDiff()

}

private lateinit var asyncListDiffer: AsyncListDiffer<DiffItem>