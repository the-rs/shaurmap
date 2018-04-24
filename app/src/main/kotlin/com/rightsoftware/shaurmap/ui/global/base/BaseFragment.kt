package com.rightsoftware.shaurmap.ui.global.base

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.rightsoftware.shaurmap.R
import com.rightsoftware.shaurmap.ui.global.ProgressDialog
import com.tingyik90.snackprogressbar.SnackProgressBar
import com.tingyik90.snackprogressbar.SnackProgressBarManager
import org.jetbrains.anko.AnkoLogger

abstract class BaseFragment : MvpAppCompatFragment(), AnkoLogger {
    companion object {
        private val PROGRESS_TAG = "base_fragment_progress"
    }

    abstract val layoutRes: Int

    private lateinit var snackProgressBarManager : SnackProgressBarManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            = inflater.inflate(layoutRes, container, false)

    protected fun setProgressVisibility(visible: Boolean) {
        val fragment = childFragmentManager.findFragmentByTag(PROGRESS_TAG)
        if (fragment != null && !visible) {
            (fragment as ProgressDialog).dismissAllowingStateLoss()
            childFragmentManager.executePendingTransactions()
        } else if (fragment == null && visible) {
            ProgressDialog().show(childFragmentManager, PROGRESS_TAG)
            childFragmentManager.executePendingTransactions()
        }
    }

    protected fun showSnackMessage(message: String) {
        view?.let {
            val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_LONG)
            val messageTextView = snackbar.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
            messageTextView.setTextColor(Color.WHITE)
            snackbar.show()
        }
    }

    protected fun showSnackProgressBar(message: String = getString(R.string.please_wait)){
        view?.let {
            snackProgressBarManager = SnackProgressBarManager(it)
                    .setProgressBarColor(R.color.colorPrimary)

            snackProgressBarManager.show(
                    SnackProgressBar(SnackProgressBar.TYPE_DETERMINATE, message)
                            .setAllowUserInput(true), SnackProgressBarManager.LENGTH_INDEFINITE)
        }
    }

    protected fun setProgress(progress : Int){
        view?.let {
            with(snackProgressBarManager){
                setProgress(progress)
                if(progress == 100) dismiss()
            }
        }
    }
}
