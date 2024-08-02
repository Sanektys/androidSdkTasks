package com.example.androidsdktasks

import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class Router(private val fragmentManager: FragmentManager, @IdRes private val container: Int) {

    fun openNextFragment(newFragment: Fragment, commitName: String? = null) {
        val fragmentsCount = fragmentManager.backStackEntryCount
        newFragment.arguments = bundleOf(KEY_FRAGMENT_NUMBER to fragmentsCount + 1)

        fragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(container, newFragment)
            .addToBackStack(commitName)
            .commit()
    }

    fun popFragment(commitName: String? = null, isInclusive: Boolean = false) {
        if (fragmentManager.backStackEntryCount > 1) {
            fragmentManager.popBackStack(
                commitName,
                if (isInclusive) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0
            )
        }
    }


    companion object {
        const val KEY_FRAGMENT_NUMBER = "fragment_number"
    }
}