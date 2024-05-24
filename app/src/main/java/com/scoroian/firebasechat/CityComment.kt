package com.scoroian.firebasechat

data class CityComment(
    val id: String = "",
    val user: String? = null,
    val comment: String? = null,
    val timestamp: Long? = null,
    val cityId: String = ""
)
