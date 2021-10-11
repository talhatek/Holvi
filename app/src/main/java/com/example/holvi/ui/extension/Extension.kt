package com.example.holvi.ui.extension

import android.content.res.Resources

val Float.toPx get() = this * Resources.getSystem().displayMetrics.density
val Float.toDp get() = this / Resources.getSystem().displayMetrics.density