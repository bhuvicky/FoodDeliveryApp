package com.bhuvanesh.appbase

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths


fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun showViews(vararg views: View) {
    // TODO: Can use forEach() instead of map ??
    views.map { it.show() }
}

fun goneViews(vararg views: View) {
    views.forEach { it.gone() }
}

// After we set the error in edittext, it won't show the error msg immediately, only if we click (after get the focus) on the ET, it will show error msg
// To avoid that, and show error msg immediately after set it, we should manually call requestFocus()
fun TextInputEditText.setErrorWithReqFocus(errorMsg: String) {
    error = errorMsg
    requestFocus()
}

inline fun Context.checkInternetAndExecute(function: () -> Unit) {
    if (isNetworkAvailable()) {
        function()
    } else {
        Toast.makeText(this, getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show()
    }
}
/*
* getActiveNetwork - Call Requires API Level 23
* getNetworkCapabilities - Call Requires API Level 21
* getActiveNetworkInfo  -  deprecated on API 29.
*
* NetworkCapabilities is not deprecated in API 29 but it requires API 21 so I have called it on API 29 only.
* However getActiveNetworkInfo() is deprecated only in API 29 and works on all APIs , so we can use it in all apis bellow 29.
* below code handles all 3 scenarios.
*
* SOURCE: https://stackoverflow.com/questions/57277759/getactivenetworkinfo-is-deprecated-in-api-29
* */
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val nw  = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    } else {
        val nwInfo = connectivityManager.activeNetworkInfo ?: return false
        return nwInfo.isConnected
    }
}

fun <ViewT : View> AppCompatActivity.bindView(@IdRes idRes: Int): Lazy<ViewT> {
    return lazy(LazyThreadSafetyMode.NONE) {
        findViewById<ViewT>(idRes)
    }
}

fun File.toByteArray()  = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    Files.readAllBytes(Paths.get(path))
} else {
    val size = length().toInt()
    val bytes = ByteArray(size)
    val bytesRead = 0
    BufferedInputStream(FileInputStream(this)).use {
        it.read(bytes, 0, bytes.size)
        bytes
    }
}

fun String.getFileNameFromPath() = this.substring(
    this.lastIndexOf('/') + 1,
    this.length)


/*
 This extension function is used to check whether the ViewModel obj should create with constructor or without constructor
 if with constructor means, our custom ViewModelFactory is used to create ViewModel object.
 if without constructor means, default ViewModelFactory will used to create ViewModel object.

 inline parameter - val create: (() -> T)? = null;  should not be nullable type   or
 make it "noiniline"
 */
inline fun <reified T : ViewModel> AppCompatActivity.getViewModel(noinline create: (() -> T)? = null): T {
    return create?.let {
        ViewModelProviders.of(this, AppViewModelFactory(create)).get(T::class.java)
    } ?: ViewModelProviders.of(this).get(T::class.java)

}

inline fun <reified T : ViewModel> Fragment.getViewModel(noinline create: (() -> T)? = null): T {
    return create?.let {
        ViewModelProviders.of(this, AppViewModelFactory(create)).get(T::class.java)
    } ?: ViewModelProviders.of(this).get(T::class.java)

}

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}

fun CharSequence.isStringEmpty(): Boolean {
    return toString().isEmpty()
}