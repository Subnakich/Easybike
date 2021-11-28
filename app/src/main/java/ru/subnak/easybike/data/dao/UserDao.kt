package ru.subnak.easybike.data.dao

import androidx.room.*
import ru.subnak.easybike.data.entity.UserDbModel

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(userDbModel: UserDbModel)
    //fun addUser(userDbModel: UserDbModel)

    @Query("SELECT * FROM users WHERE userID=:userID LIMIT 1")
    suspend fun getUser(userID: Int): UserDbModel
    //fun getUser(userID: Int): UserDbModel
}