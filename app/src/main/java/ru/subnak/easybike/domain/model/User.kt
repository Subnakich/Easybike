package ru.subnak.easybike.domain.model

data class User(
    val name: String,
    val age: Int,
    val weight: Int,
    val height: Int,
    val sex: String,
    var userID: Int = UNDEFINED_ID
) {

    companion object {

        const val UNDEFINED_ID = 1
    }
}
