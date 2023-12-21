package com.example.sistemielektronikperkomunikiminestudenteve.Models

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.util.Date

data class GetPostsModel (
    var title: String? = null,
    var desc: String? = null,
    var poster: String? = null,
    var likes: String? = null,
    var comments: String? = null,
    var publicKey:String? = null,
    var timestamp: Long? = null,
    var posttime: String? = null
)