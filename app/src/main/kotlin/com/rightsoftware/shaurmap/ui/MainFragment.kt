package com.rightsoftware.shaurmap.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import com.rightsoftware.shaurmap.R
import com.rightsoftware.shaurmap.ui.global.base.BaseFragment
import com.rightsoftware.shaurmap.ui.favorites.FavoritesFragment
import com.rightsoftware.shaurmap.ui.map.MapFragment
import com.rightsoftware.shaurmap.ui.profile.ProfileFragment
import com.rightsoftware.shaurmap.utils.extensions.transaction
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_main

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bottomNavigation.setOnNavigationItemSelectedListener {
            showTab(it.itemId)
            true
        }

        if (savedInstanceState == null) {
            bottomNavigation.selectedItemId = R.id.action_map
        }
    }

    private fun showTab(id: Int) {
        childFragmentManager.transaction {
            findFragment(R.id.action_map)?.let {
                if(it.isVisible)
                    hide(it)
            }
            findFragment(R.id.action_profile)?.let {
                if(it.isVisible)
                    hide(it)
            }
            findFragment(R.id.action_favorites)?.let {
                if(it.isVisible)
                    hide(it)
            }

            findFragment(id).let {
                when (it) {
                    null -> add(R.id.fragmentContainer, createFragment(id), idToTag(id))
                    else -> show(it)
                }
            }
        }
    }

    private fun idToTag(id: Int) = "tab_$id"

    private fun findFragment(id: Int) = childFragmentManager.findFragmentByTag(idToTag(id))

    private fun createFragment(id: Int) : Fragment {
        return when (id) {
            R.id.action_map -> MapFragment()
            R.id.action_profile -> ProfileFragment()
            R.id.action_favorites -> FavoritesFragment()

            else -> throw IllegalArgumentException("No fragment with id $id")
        }
    }
}
