package com.scoroian.firebasechat

data class ChatMessage(
    val user: String? = null,
    val message: String? = null,
    val timestamp: Long? = null
)
