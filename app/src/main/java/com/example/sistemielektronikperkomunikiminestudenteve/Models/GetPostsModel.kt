package com.example.sistemielektronikperkomunikiminestudenteve.Models

data class GetPostsModel (
    var title: String? = null,
    var desc: String? = null,
    var poster: String? = null,
    var profileURL: String? = null,
    var posterID: String? = null,
    var likes: String? = null,
    var comments: String? = null,
    var publicKey:String? = null,
    var commentTimeStamp: Long? = null,
    var posttime: String? = null
)