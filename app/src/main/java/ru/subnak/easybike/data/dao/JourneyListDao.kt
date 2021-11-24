package ru.subnak.easybike.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.subnak.easybike.data.entity.JourneyDbModel

@Dao
interface JourneyListDao {
    @Query("SELECT * FROM journeys")
    fun getJourneyList(): LiveData<List<JourneyDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addJourney(journeyDbModel: JourneyDbModel)

    @Query("DELETE FROM journeys WHERE id=:journeyID")
    suspend fun deleteJourney(journeyID: Int)

    @Query("SELECT * FROM journeys WHERE id=:journeyID LIMIT 1")
    suspend fun getJourney(journeyID: Int): JourneyDbModel
}