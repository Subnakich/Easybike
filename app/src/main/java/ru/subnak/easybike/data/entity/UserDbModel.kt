package ru.subnak.easybike.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class UserDbModel(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val userID: Int,
    val name: String,
    val age: Int,
    val weight: Int,
    val height: Int,
    val sex: String,
)
