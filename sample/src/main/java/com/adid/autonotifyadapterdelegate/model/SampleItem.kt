package com.adid.autonotifyadapterdelegate.model

import com.adid.adapterdelegate.DiffItem

class SampleItem(
    val id: Int,
    var name: String,
    var color: Int
) : DiffItem {

    override fun getItemId() = name
    override fun getDiff() = name

}