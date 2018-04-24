package com.rightsoftware.shaurmap.utils.extensions

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int){
    supportFragmentManager.transaction { add(frameId, fragment) }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.transaction{replace(frameId, fragment)}
}

fun AppCompatActivity.showKeyboard() {
    val view = this.currentFocus ?: return

    try {
        val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    } catch (e: Exception) {
        // ignored
    }

}

fun AppCompatActivity.hideKeyboard() {
    val view = this.currentFocus ?: return

    try {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (!imm.isActive) {
            return
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    } catch (e: Exception) {
        // ignored
    }

}

fun AppCompatActivity.isKeyboardShown(): Boolean {
    val view = this.currentFocus ?: return false

    try {
        val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputManager.isActive(view)
    } catch (e: Exception) {
        // ignored
    }

    return false
}

fun Context.getResColor(colorId: Int) = ContextCompat.getColor(this, colorId)