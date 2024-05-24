package com.scoroian.firebasechat

data class ChatMessage(
    val id: String = "",
    val user: String? = null,
    val message: String? = null,
    val timestamp: Long? = null,
    val cityId: String = ""
)
