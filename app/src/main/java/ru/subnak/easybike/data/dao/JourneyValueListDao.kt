package ru.subnak.easybike.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.subnak.easybike.data.entity.JourneyValueDbModel
import ru.subnak.easybike.data.relation.JourneyWithJourneyValueList

@Dao
interface JourneyValueListDao {
    @Query("SELECT * FROM journey_values")
    fun getJourneyValueList(): LiveData<List<JourneyValueDbModel>>

    @Transaction
    @Query("SELECT * FROM journeys WHERE id IN (SELECT DISTINCT(journey_id) FROM journey_values)")
    fun getJourneyWithValues(): LiveData<List<JourneyWithJourneyValueList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addJourneyValue(journeyValueDbModel: JourneyValueDbModel)

    @Query("DELETE FROM journey_values WHERE id=:valueID AND journey_id=:journeyID")
    suspend fun deleteJourneyValue(valueID: Int, journeyID: Int)

    @Query("SELECT * FROM journey_values WHERE id=:valueID AND journey_id=:journeyID LIMIT 1")
    suspend fun getJourneyValue(valueID: Int, journeyID: Int): JourneyValueDbModel
}