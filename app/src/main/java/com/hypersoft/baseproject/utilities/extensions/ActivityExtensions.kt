package com.hypersoft.baseproject.utilities.extensions

import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

/* ---------------------------------------------- BackPress ---------------------------------------------- */

fun AppCompatActivity.onBackPressedDispatcher(callback: () -> Unit) {
    onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            callback.invoke()
        }
    })
}