package com.rightsoftware.shaurmap.ui.global.list

import android.view.ViewGroup
import com.rightsoftware.shaurmap.R
import com.rightsoftware.shaurmap.utils.extensions.animateFadeIn
import com.rightsoftware.shaurmap.utils.extensions.animateFadeOut
import kotlinx.android.synthetic.main.layout_empty_list.view.*

class EmptyListViewHolder(
        private val view: ViewGroup,
        private val refreshListener: () -> Unit = {}
) {
    private val res = view.resources

    init {
        view.btnRefresh.setOnClickListener { refreshListener() }
    }

    fun showEmptyData(msg: String? = null) {
        view.tvErrorDescription.text =  msg ?: res.getText(R.string.nothing_here_yet)
        view.animateFadeIn(300)
    }

    fun showEmptyError(msg: String? = null) {
        view.tvErrorDescription.text = msg ?: res.getText(R.string.something_went_wrong)
        view.animateFadeIn(300)
    }

    fun hide() {
        view.animateFadeOut(300)
    }
}