package com.beetleware.ugt.utils.extension

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.beetleware.tabibyLive.common.extensions.popBackStack
import com.beetleware.ugt.R
import com.beetleware.ugt.network.models.ApiResponse
import com.beetleware.ugt.network.models.ErrorResponse
import com.beetleware.ugt.ui.activities.SplashActivity
import com.beetleware.ugt.utils.Config
import com.beetleware.ugt.utils.Config.SNAK_BAR_DURATION
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.json.JSONObject
import java.io.IOException


fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, message, duration).show()

fun Activity.getThisColor(@ColorRes id: Int) = ContextCompat.getColor(baseContext, id)


fun Activity.errorMsg(msg: String, duration: Int = Config.SNAK_BAR_DURATION) {
    val snackbar = Snackbar.make(window.decorView.findViewById(android.R.id.content)!!, msg, Snackbar.LENGTH_LONG)
    snackbar.view.setBackgroundColor(getThisColor(android.R.color.holo_red_dark)!!)
    val textView =
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    textView.setTextColor(getThisColor(android.R.color.white)!!)
    snackbar.show()
}

fun Activity.errorMsg(@StringRes msgId: Int, duration: Int = Config.SNAK_BAR_DURATION) {
    errorMsg(getString(msgId), duration)
}
fun Activity.successrMsg(msg: String, duration: Int = Config.SNAK_BAR_DURATION) {
    val snackbar = Snackbar.make(window.decorView.findViewById(android.R.id.content)!!, msg, Snackbar.LENGTH_LONG)
    snackbar.view.setBackgroundColor(getThisColor(android.R.color.holo_green_light)!!)
    val textView =
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    textView.setTextColor(getThisColor(android.R.color.white)!!)
    snackbar.show()
}

fun Activity.successrMsg(@StringRes msgId: Int, duration: Int = Config.SNAK_BAR_DURATION) {
    errorMsg(getString(msgId), duration)
}
fun Activity.showDialog(
    dialogFragment: DialogFragment, bundle: Bundle? = null,
    tag: String? = dialogFragment::class.java.simpleName
) {

    if (dialogFragment.isAdded) return

    dialogFragment.arguments = bundle
    return dialogFragment.show((this as FragmentActivity).supportFragmentManager, tag)
}


inline fun Activity.alertDialog(body: AlertDialog.Builder.() -> AlertDialog.Builder): AlertDialog {
    return AlertDialog.Builder(this)
        .body()
        .show()
}

fun Activity.goToActivity(activityClass: Class<*>) = this.startActivity(Intent(this, activityClass))
fun Activity.goToActivity(activityClass: Class<*>, bundle: Bundle?) =
    this.startActivity(Intent(this, activityClass).putExtras(bundle!!))

fun Activity.addFragment(
    @IdRes id: Int, fragment: Fragment, bundle: Bundle? = null,
    addToStack: Boolean = false, tag: String? = fragment::class.java.simpleName
): Int {

    fragment.arguments = bundle

    return if (addToStack)
        (this as FragmentActivity)
            .supportFragmentManager
            .beginTransaction()
            .add(id, fragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    else
        (this as FragmentActivity)
            .supportFragmentManager
            .beginTransaction()
            .add(id, fragment, tag)
            .commitAllowingStateLoss()
}

fun Activity.replaceFragment(
    @IdRes id: Int, fragment: Fragment, bundle: Bundle? = null,
    addToStack: Boolean = false, tag: String? = fragment::class.java.simpleName
): Int {

    fragment.arguments = bundle

    return if (addToStack)
        (this as FragmentActivity)
            .supportFragmentManager
            .beginTransaction()
            .replace(id, fragment, tag)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    else
        (this as FragmentActivity)
            .supportFragmentManager
            .beginTransaction()
            .replace(id, fragment, tag)
            .commitAllowingStateLoss()
}

fun Activity.popBackStack(fragment: Fragment? = null): Boolean {
    return (this as FragmentActivity)
        .supportFragmentManager
        .popBackStackImmediate()
}


fun Activity.popAllFragments() {
    for (fragment in (this as FragmentActivity).supportFragmentManager.fragments) {
        fragment.popBackStack()
    }

}
 fun Activity.hideKeyboard() {
    val view = currentFocus

    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Activity.handleErrorResponse(response: ApiResponse<*>) {

    if (response.isResponseSuccessful)
        return

    if (response.exception != null) {
        if (response.isCanceled) return
        hideKeyboard()

        when (response.exception) {
            is IOException -> errorMsg(R.string.msg_error_connection)
            else -> {
                // errorMsg(R.string.msg_something_error)
            }
        }

    } else if (!response.isResponseSuccessful) {
        hideKeyboard()

        when (response.responseCode) {
            500, 503 -> errorMsg(R.string.msg_server_error)
            404 -> {
                try {
                    val jsonObject = JSONObject(response.errorBody!!.string())
                    val userMessage = jsonObject.getString("msg")
                    errorMsg(userMessage,SNAK_BAR_DURATION)
                }catch (e:Exception){

                }
            }

            504 -> errorMsg(R.string.no_internet)
            401 -> {
                if (this !is SplashActivity){
                    goToActivity(SplashActivity::class.java)
                    this?.finish()
                } else {
                    try {
                        val jsonObject = JSONObject(response.errorBody!!.string())
                        val userMessage = jsonObject.getString("msg")
                        errorMsg(userMessage)
                    }catch (e:Exception){

                    }
                }
            }
            422 -> {
                try {
                    val error = Gson().fromJson<ErrorResponse>(
                        response.errorBody?.string(),
                        ErrorResponse::class.java
                    )
                   // errorMsg(error.message.values.first { true }[0])
                } catch (e: Exception) {
//                        errorMsg(R.string.msg_something_error)
                }
            }
            444 -> {
                try {
                    val jsonObject = JSONObject(response.errorBody!!.string())
                    val userMessage = jsonObject.getString("msg")
                    errorMsg(userMessage)
                }catch (e:Exception){

                }
            }
            302 ->{

                try {
                    val jsonObject = JSONObject(response.errorBody!!.string())
                    val userMessage = jsonObject.getString("msg")
                    errorMsg(userMessage)
                }catch (e:Exception){

                }
            }
            400 ->{
                try {
                    val jsonObject = JSONObject(response.errorBody!!.string())
                    val userMessage = jsonObject.getString("msg")
                    errorMsg(userMessage)
                }catch (e:Exception){
                    try {
                        errorMsg(JSONObject(response.errorBody!!.string()).toString())
                    }catch (e:Exception){

                    }
                }
            }
            else -> {
                try {
                    val jsonObject = JSONObject(response.errorBody!!.string())
                    val userMessage = jsonObject.getString("msg")
                    errorMsg(userMessage)
                }catch (e:Exception){

                }
            }
        }
    }
}






