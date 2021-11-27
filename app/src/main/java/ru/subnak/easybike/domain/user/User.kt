package ru.subnak.easybike.domain.user

data class User(
    val userID: Int,
    val name: String,
    val age: Int,
    val weight: Int,
    val height: Int,
    val sex: String,
)
