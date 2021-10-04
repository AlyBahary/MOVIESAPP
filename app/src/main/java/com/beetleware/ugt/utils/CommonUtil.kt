package com.beetleware.ugt.utils

import android.app.Dialog
import android.content.ContentUris
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.beetleware.ugt.R
import java.io.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*
import java.util.regex.Pattern


class CommonUtil {
    fun printTextLong(text: String) {
        Toast.makeText(AndroidApplication.appContext, "" + text, Toast.LENGTH_LONG)
            .show()
    }

    fun printTextShort(text: String) {
        Toast.makeText(AndroidApplication.appContext, "" + text, Toast.LENGTH_SHORT)
            .show()
    }

    companion object {
        fun makeDefaultLocaleToArabic(context: Context) {
            val sharedPref: SharedPreferences =
                context.getSharedPreferences(Config.PREFS_NAME, 0)
            AndroidApplication.setAppLocale(
                sharedPref.getString(
                    Config.APPLANGUAGE,
                    "ar"
                )
            )
        }

        fun commonSharedPref(): SharedPreferences {
            return AndroidApplication.appContext!!.getSharedPreferences(Config.PREFS_NAME, 0)
        }

        fun getDataFromSharedPref(key: String?): String {
            val pref: SharedPreferences =
                AndroidApplication.appContext!!.getSharedPreferences(Config.PREFS_NAME, 0)
            return pref.getString(key, "")!!
        }

        val currentLang: String
            get() {
                val pref: SharedPreferences =
                    AndroidApplication.appContext!!.getSharedPreferences(Config.PREFS_NAME, 0)
                return pref.getString(Config.APPLANGUAGE, Locale.getDefault().language)!!
            }

        fun addToSharedPref(key: String?, value: String?) {
            val sharedpreferences: SharedPreferences = AndroidApplication.appContext!!
                .getSharedPreferences(
                    Config.PREFS_NAME,
                    Context.MODE_PRIVATE
                )
            val editor: SharedPreferences.Editor = sharedpreferences.edit()
            editor.putString(key, value)
            editor.commit()
        }

        fun logout() {
            val pref: SharedPreferences = AndroidApplication.appContext!!
                .getSharedPreferences(Config.PREFS_NAME, 0)
            val editor: SharedPreferences.Editor = pref.edit()
            editor.remove(Config.ACCESS_TOKEN)
            editor.apply()
            editor.commit()
        }

        @Throws(IOException::class)
        fun readIt(stream: InputStream?, len: Long): String {
            var n = 0
            val buffer = CharArray(1024 * 4)
            val reader = InputStreamReader(stream, "UTF8")
            val writer = StringWriter()
            while (-1 != reader.read(buffer).also { n = it }) writer.write(buffer, 0, n)
            return writer.toString()
        }


        fun getCompleteAddressString(
            context: Context?,
            LATITUDE: Double,
            LONGITUDE: Double
        ): String {
            val geocoder: Geocoder
            var strAdd = ""
            val loc = Locale("ar")
            geocoder = Geocoder(context, loc)
            try {
                val addresses: List<Address> = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
                if (addresses != null) {
                    val returnedAddress = addresses[0]
                    val strReturnedAddress = StringBuilder("")
                    for (i in 0..returnedAddress.maxAddressLineIndex) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                    }
                    strAdd = strReturnedAddress.toString()
                } else {
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return strAdd
        }

        fun isValidEmail(target: String?): Boolean {
            return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }

        fun validatePhoneNumber(phoneNumber: String?): Boolean {
            val pattern =
                Pattern.compile("^(009665|9665|\\+9665|05|5)(5|0|3|6|4|9|1|8|7)([0-9]{7})$")
            return pattern.matcher(phoneNumber).matches()
        }

        fun validateIdNumber(phoneNumber: String?): Boolean {
            val pattern =
                Pattern.compile("^[0-1].{9}$")
            return pattern.matcher(phoneNumber).matches()
        }

        fun isValidComplexPassword(password: String) =
            Pattern.compile("^(?=.*\\d)(?=.*[a-zA-Z\\u0621-\\u064A]).{8,}$").matcher(password)
                .matches()

        fun calculateNoOfColumns(
            context: Context,
            columnWidthDp: Float
        ): Int { // For example columnWidthdp=180
            val displayMetrics: DisplayMetrics = context.resources.displayMetrics
            val screenWidthDp: Float = displayMetrics.widthPixels / displayMetrics.density
            return (screenWidthDp / (columnWidthDp + 0.5)).toInt()
        }

        fun currencyFormat(currency: Double): String {
            val symbols = DecimalFormatSymbols(Locale.US)

            val formatter: NumberFormat = DecimalFormat("#,###", symbols)

            return formatter.format(currency)
        }


        fun sucessDialoge(context: Context, string: String): Dialog {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.confirmation_dialgoe)
            dialog.setCancelable(true)

            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialog.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            (dialog.findViewById<View>(R.id.textView) as TextView).text = string
            dialog.show()
            dialog.window!!.attributes = lp
            return dialog
        }

        // get real file path
        fun getRealPath(context: Context, uri: Uri): String? {
            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri: Uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)
                    )
                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(
                        split[1]
                    )
                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }
            } else if ("content".equals(uri.getScheme(), ignoreCase = true)) {
                return getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.getScheme(), ignoreCase = true)) {
                return uri.getPath()
            }
            return null
        }

        /**
         * Get the value of the data column for this Uri. This is useful for
         * MediaStore Uris, and other file-based ContentProviders.
         *
         * @param context The context.
         * @param uri The Uri to query.
         * @param selection (Optional) Filter used in the query.
         * @param selectionArgs (Optional) Selection arguments used in the query.
         * @return The value of the _data column, which is typically a file path.
         */
        fun getDataColumn(
            context: Context, uri: Uri?, selection: String?,
            selectionArgs: Array<String>?
        ): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(
                column
            )
            try {
                cursor = context.contentResolver.query(
                    uri!!, projection, selection, selectionArgs,
                    null
                )
                if (cursor != null && cursor.moveToFirst()) {
                    val column_index: Int = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(column_index)
                }
            } finally {
                if (cursor != null) cursor.close()
            }
            return null
        }


        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.getAuthority()
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.getAuthority()
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.getAuthority()
        }

    }
}