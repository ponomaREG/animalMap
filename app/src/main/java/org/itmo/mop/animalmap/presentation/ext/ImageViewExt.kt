package org.itmo.mop.animalmap.presentation.ext

import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

fun ImageView.loadByUrl(url: String) {
    val circularProgressIndicator = CircularProgressDrawable(context).also {
        it.strokeWidth = 5f
        it.centerRadius = 30f
        it.start()
    }
    Glide.with(context)
        .load(url)
        .placeholder(circularProgressIndicator)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .into(this)
}