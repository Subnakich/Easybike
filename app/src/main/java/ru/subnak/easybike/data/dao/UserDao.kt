package ru.subnak.easybike.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.subnak.easybike.data.entity.UserDbModel

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(userDbModel: UserDbModel)

    @Query("SELECT * FROM users WHERE id=:userID LIMIT 1")
    suspend fun getUser(userID: Int): UserDbModel

    @Query("SELECT EXISTS (SELECT 1 FROM users WHERE id=:userID LIMIT 1 )")
    suspend fun checkUser(userID: Int): Int
}