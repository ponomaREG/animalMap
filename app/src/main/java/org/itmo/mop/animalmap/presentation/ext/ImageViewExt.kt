package org.itmo.mop.animalmap.presentation.ext

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadByUrl(url: String) {
    Glide.with(context)
        .load(url)
        .into(this)
}