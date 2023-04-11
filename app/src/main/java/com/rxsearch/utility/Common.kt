package com.rxsearch.utility

import android.view.View
import android.widget.ProgressBar

object Common {
     fun showProgressBar(progressBar: ProgressBar, showProgressBar: Boolean) {
        progressBar.visibility = if (showProgressBar) View.VISIBLE else View.GONE
    }
}