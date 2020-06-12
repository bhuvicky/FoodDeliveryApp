package com.bhuvanesh.appbase.ui

import androidx.appcompat.app.AppCompatActivity
import com.bhuvanesh.appbase.R

open class BaseActivity: AppCompatActivity() {

    private val fm = supportFragmentManager

    fun replace(containerId: Int, fragment: BaseFragment, doAnimation: Boolean = true) {
        with(fm.beginTransaction()) {
            if (doAnimation)
                setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
            replace(containerId, fragment)
            addToBackStack(null)
            commit()
        }
    }

    fun setTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onBackPressed() {
        if (fm.backStackEntryCount > 1) {
            fm.popBackStack()
        } else {
            finish()
        }
    }
}