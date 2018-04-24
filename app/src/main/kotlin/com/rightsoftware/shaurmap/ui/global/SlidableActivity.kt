package com.rightsoftware.shaurmap.ui.global

import android.os.Bundle
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrInterface
import com.r0adkll.slidr.model.SlidrListener
import com.r0adkll.slidr.model.SlidrPosition
import com.rightsoftware.shaurmap.ui.global.base.BaseActivity
import com.rightsoftware.shaurmap.utils.extensions.hideKeyboard
import com.rightsoftware.shaurmap.utils.extensions.isKeyboardShown

abstract class SlidableActivity : BaseActivity() {

    protected open val sliderPosition : SlidrPosition = SlidrPosition.LEFT
    protected lateinit var slider: SlidrInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sliderConfig = SlidrConfig.Builder()
                .position(sliderPosition)
                .listener(object: SlidrListener {
                    override fun onSlideClosed() { }
                    override fun onSlideChange(percent: Float) { }
                    override fun onSlideOpened() { }

                    override fun onSlideStateChanged(state: Int) {
                        if(isKeyboardShown())
                            hideKeyboard()
                    }
                })
                .build()

        slider = Slidr.attach(this, sliderConfig)
    }
}