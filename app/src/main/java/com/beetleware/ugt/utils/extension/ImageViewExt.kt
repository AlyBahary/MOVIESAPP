package com.beetleware.tabibyLive.common.extensions

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.io.File


fun ImageView.loadImg(
    @Nullable photo: String, @DrawableRes placeHolder: Int,
    @DrawableRes error: Int = placeHolder
) = loadImg(Uri.parse(photo), placeHolder, error)



fun ImageView.loadImg(
    @Nullable photo: Uri, @DrawableRes placeHolder: Int,
    @DrawableRes error: Int = placeHolder
) {

    val myOptions = RequestOptions()
        .override(this.width, this.height)
        .fitCenter()

    Glide.with(context!!)
        .load(photo)
        .placeholder(placeHolder)
        .error(error)
        .fallback(error)
        .apply(myOptions)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}
fun ImageView.loadImg(
    @Nullable photo: File, @DrawableRes placeHolder: Int,
    @DrawableRes error: Int = placeHolder
) {

    val myOptions = RequestOptions()
        .override(this.width, this.height)
        .fitCenter()


    Glide.with(context!!)
        .load(photo)
        .placeholder(placeHolder)
        .error(error)
        .fallback(error)
        .apply(myOptions)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

