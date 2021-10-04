package com.beetleware.tabibyLive.common.extensions

import android.app.AlertDialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.beetleware.ugt.utils.Config
import com.beetleware.ugt.R
import com.beetleware.ugt.utils.extension.*
import com.google.android.material.snackbar.Snackbar


fun Fragment.toast(message: CharSequence, duration: Int = Toast.LENGTH_LONG) =
    activity?.toast(message, duration)

fun Fragment.getThisColor(@ColorRes id: Int) = activity?.getThisColor(id)

inline fun Fragment.alertDialog(body: AlertDialog.Builder.() -> AlertDialog.Builder) =
    activity?.alertDialog(body)

fun Fragment.goToActivity(activityClass: Class<*>) = activity?.goToActivity(activityClass)
fun Fragment.goToActivity(activityClass: Class<*>, bundle: Bundle?) =
    activity?.goToActivity(activityClass, bundle)

fun Fragment.addFragment(
    @IdRes id: Int, fragment: Fragment, bundle: Bundle? = null,
    addToStack: Boolean = true,
    tag: String? = fragment::class.java.simpleName
) = activity?.addFragment(id, fragment, bundle, addToStack, tag)

fun Fragment.replaceFragment(
    @IdRes id: Int, fragment: Fragment, bundle: Bundle? = null,
    addToStack: Boolean = false,
    tag: String? = fragment::class.java.simpleName
) = activity?.replaceFragment(id, fragment, bundle, addToStack, tag)

fun Fragment.popBackStack(fragment: Fragment = this) = activity?.popBackStack(fragment)


fun Fragment.showDialog(
    dialogFragment: DialogFragment, bundle: Bundle? = null,
    tag: String? = dialogFragment::class.java.simpleName
) = activity?.showDialog(dialogFragment, bundle, tag)


fun Fragment.errorMsg(msg: String, duration: Int = Config.SNAK_BAR_DURATION) {
    val snackbar = Snackbar.make(view!!, msg, Snackbar.LENGTH_LONG)
    snackbar.view.setBackgroundColor(getThisColor(android.R.color.holo_red_dark)!!)
    val textView =
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    textView.setTextColor(getThisColor(android.R.color.white)!!)
    snackbar.show()
}

fun Fragment.errorMsg(@StringRes msgId: Int, duration: Int = Config.SNAK_BAR_DURATION) {
    errorMsg(getString(msgId), duration)
}

fun Fragment.warningMsg(msg: String, duration: Int = Config.SNAK_BAR_DURATION) {
    val snackbar = Snackbar.make(view!!, msg, Snackbar.LENGTH_LONG)
    snackbar.view.setBackgroundColor(getThisColor(android.R.color.holo_orange_light)!!)
    val textView =
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    textView.setTextColor(getThisColor(android.R.color.black)!!)
    snackbar.show()
}

fun Fragment.warningMsg(@StringRes msgId: Int, duration: Int = Config.SNAK_BAR_DURATION) {
    warningMsg(getString(msgId), duration)
}

fun Fragment.confirmMsg(msg: String) {
    val snackbar = Snackbar.make(view!!, msg, Snackbar.LENGTH_SHORT)
    snackbar.view.setBackgroundColor(getThisColor(android.R.color.holo_green_dark)!!)
    val textView = snackbar.view.findViewById(R.id.snackbar_text) as TextView
    textView.setTextColor(getThisColor(android.R.color.black)!!)
    val params = snackbar.view.layoutParams as ViewGroup.MarginLayoutParams
//    params.setMargins(
//            10, 0,
//            10,
//            activity!!.findViewById<BottomNavigationView>(R.id.navigationView).height + 20)

    snackbar.view.layoutParams = params
    snackbar.view.setBackgroundResource(R.drawable.snackbar_radius)
    snackbar.show()
}


fun Fragment.confirmMsg(@StringRes msgId: Int, duration: Int = Config.SNAK_BAR_DURATION) {
    warningMsg(getString(msgId), duration)

}
