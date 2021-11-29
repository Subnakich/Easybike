package ru.subnak.easybike.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.subnak.easybike.data.entity.JourneyValueDbModel

@Dao
interface JourneyValueListDao {

    @Query("SELECT * FROM journey_values WHERE id=:journeyID")
    fun getJourneyValueList(journeyID: Int): LiveData<List<JourneyValueDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addJourneyValue(journeyValueDbModel: JourneyValueDbModel)
}