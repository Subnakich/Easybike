package ru.subnak.easybike.domain.user

import ru.subnak.easybike.domain.journey.Journey

data class User(
    val userID: Int,
    val name: String,
    val age: Int,
    val weight: Int,
    val height: Int,
    val sex: String,
    val journeys: List<Journey> ?= null
)
