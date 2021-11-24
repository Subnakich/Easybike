package ru.subnak.easybike.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.subnak.easybike.data.entity.JourneyDbModel
import ru.subnak.easybike.data.relation.JourneyWithJourneyValueList

@Dao
interface JourneyListDao {
    @Query("SELECT * FROM journeys")
    fun getJourneyList(): LiveData<List<JourneyDbModel>>

    @Query("SELECT * FROM journeys")
    fun getJourneyWithJourneyValuesList(): LiveData<List<JourneyWithJourneyValueList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addJourney(journeyDbModel: JourneyDbModel)

    @Query("DELETE FROM journeys WHERE id=:journeyID")
    suspend fun deleteJourney(journeyID: Int)

    @Query("SELECT * FROM journeys WHERE id=:journeyID LIMIT 1")
    suspend fun getJourney(journeyID: Int): JourneyDbModel
}