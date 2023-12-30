package com.example.sistemielektronikperkomunikiminestudenteve.Models

data class GetNotificationsModel(
    var notificationSenderID: String? = null,//id e user qe ka ba like/comment (duhet mu kan different from current log in)
    var notificationSenderName: String? = null,// emri i user qe e ka ba like/comment
    var notificationOfPost: String? = null,//id e postimit prej ku ka ardh notification
    var notificationType: String? = null,//like ose comment
    var notificationTime: String? = null,//time qe u ardh notification
    var notificationSenderProfileURL: String? = null,//profile pic

)