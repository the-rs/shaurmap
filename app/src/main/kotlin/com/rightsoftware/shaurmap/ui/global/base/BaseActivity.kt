package com.rightsoftware.shaurmap.ui.global.base

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.rightsoftware.shaurmap.ui.global.ProgressDialog
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.contentView

abstract class BaseActivity : MvpAppCompatActivity(), AnkoLogger {
    protected abstract val layoutRes: Int

    companion object {
        private val PROGRESS_TAG = "base_activity_progress"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
    }

    protected fun setProgressVisibility(visible: Boolean) {
        val fragment = supportFragmentManager.findFragmentByTag(PROGRESS_TAG)
        if (fragment != null && !visible) {
            (fragment as ProgressDialog).dismissAllowingStateLoss()
            supportFragmentManager.executePendingTransactions()
        } else if (fragment == null && visible) {
            ProgressDialog().show(supportFragmentManager, PROGRESS_TAG)
            supportFragmentManager.executePendingTransactions()
        }
    }

    protected fun showSnackMessage(message: String) {
        contentView?.let {
            val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_LONG)
            val messageTextView = snackbar.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
            messageTextView.setTextColor(Color.WHITE)
            snackbar.show()
        }
    }
}