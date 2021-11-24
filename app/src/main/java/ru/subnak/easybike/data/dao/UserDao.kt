package ru.subnak.easybike.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.subnak.easybike.data.entity.UserDbModel
import ru.subnak.easybike.data.relation.UserWithJourneyAndJourneyValueList

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getUserWithJourneyAndJourneyValueList(): LiveData<List<UserWithJourneyAndJourneyValueList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(userDbModel: UserDbModel)

    @Query("DELETE FROM users WHERE id=:userID")
    suspend fun deleteUser(userID: Int)

    @Query("SELECT * FROM users WHERE id=:userID LIMIT 1")
    suspend fun getUser(userID: Int): UserDbModel
}