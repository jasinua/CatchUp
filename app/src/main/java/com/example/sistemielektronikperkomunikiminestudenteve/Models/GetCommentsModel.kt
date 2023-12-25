package com.example.sistemielektronikperkomunikiminestudenteve.Models

data class GetCommentsModel(

    var commentDescription:String? = null,
    var commentLike:String? = null,
    var commenterProfileURL:String? = null,
    var commentuserID:String? = null,
    var commentuserName:String? = null,
    var commentTimeStamp: Long? = null,
    var commentTime: String? = null

)