package com.example.mealzapp.utils

import android.app.Activity
import android.view.View
import android.widget.Toast


fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.disable() {
    isEnabled = false
}

fun View.enabled() {
    isEnabled = true
}

fun Activity.toast(msg: String?) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}