package com.example.sistemielektronikperkomunikiminestudenteve.Models

import android.view.View
import androidx.recyclerview.widget.RecyclerView

data class GetPostsModel (
    var title: String? = null,
    var desc: String? = null,
    var likes: String? = null,
    var comments: String? = null,
    var timestamp: Long? = null
)