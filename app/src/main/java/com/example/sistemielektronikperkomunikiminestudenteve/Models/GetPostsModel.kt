package com.example.sistemielektronikperkomunikiminestudenteve.Models

import android.view.View
import androidx.recyclerview.widget.RecyclerView

data class GetPostsModel (
    var TITLE: String? = null,
    var DESC: String? = null,
    var LIKES: String? = null,
    var COMMENTS: String? = null
)