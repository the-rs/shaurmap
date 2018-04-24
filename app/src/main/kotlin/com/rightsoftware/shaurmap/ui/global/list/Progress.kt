package com.rightsoftware.shaurmap.ui.global.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.rightsoftware.shaurmap.R
import com.rightsoftware.shaurmap.utils.extensions.inflate

class ProgressItem

class ProgressAdapterDelegate : AdapterDelegate<MutableList<Any>>() {
    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean {
        return items[position] is ProgressItem
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ProgressViewHolder(parent.inflate(R.layout.item_progress))
    }

    override fun onBindViewHolder(items: MutableList<Any>,
                                  position: Int,
                                  holder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) {}

    private class ProgressViewHolder(view: View) : RecyclerView.ViewHolder(view)
}