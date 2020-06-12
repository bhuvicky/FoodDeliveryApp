package com.bhuvanesh.appbase.ui

import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable


open class BaseFragment: Fragment() {

    protected val  disposables = CompositeDisposable()

    protected fun replace(containerId: Int, fragment: BaseFragment, doAnimation: Boolean = true) {
        (activity as BaseActivity).replace(containerId, fragment, doAnimation)
    }

    protected fun setTitle(title: String) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).setTitle(title)
        }
    }

    protected fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
    }

    protected fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.dispose()
    }

    protected open fun hasPermission(permissions: Array<String>): Boolean {
        var permissionCheck = PackageManager.PERMISSION_GRANTED
        for (permission in permissions) {
            permissionCheck += ContextCompat.checkSelfPermission(activity!!, permission!!)
        }
        return permissionCheck == PackageManager.PERMISSION_GRANTED
    }

    protected open fun requestAppPermissions(
        permissions: Array<String>?,
        requestCode: Int
    ) {
        requestPermissions(permissions!!, requestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var permissionCheck = PackageManager.PERMISSION_GRANTED
        for (result in grantResults) {
            permissionCheck += result
        }
        if (grantResults.size > 0 && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted(requestCode)
        }
    }

    protected open fun onPermissionsGranted(requestCode: Int) {

    }

}