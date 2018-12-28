package com.adid.autonotifyadapterdelegate

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.adid.autonotifyadapterdelegate.model.SampleItem
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class SampleActivity : AppCompatActivity() {

    private val sampleAdapter by lazy {
        SampleAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecycler()

        generateButton.setOnClickListener {
            generateList()
        }
    }

    private fun initRecycler() {
        recyclerView.apply {
            adapter = sampleAdapter
            layoutManager = object : GridLayoutManager(this@SampleActivity, 6) {
                override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
                    try {
                        super.onLayoutChildren(recycler, state)
                    } catch (e: IndexOutOfBoundsException) {
                        Log.e("TAG", "meet a IOOBE in RecyclerView")
                    }
                }
            }
        }
    }

    private fun generateList() {
        val r = Random()

        val listElements = mutableListOf<SampleItem>()
        for (i in 0 until 36) {
            val char = (r.nextInt(4).plus('a'.toByte())).toChar()
            listElements.add(SampleItem(i, char.toString().toUpperCase(), getColorForChar(char)))
        }

        sampleAdapter.setItems(listElements)
    }

    private fun getColorForChar(char: Char): Int {
        return when (char) {
            'a' -> R.color.colorRed
            'b' -> R.color.colorGreen
            'c' -> R.color.colorBlue
            else -> R.color.colorYellow
        }
    }
}
