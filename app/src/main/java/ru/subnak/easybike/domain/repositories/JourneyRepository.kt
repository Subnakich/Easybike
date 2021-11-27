package ru.subnak.easybike.domain.repositories

import androidx.lifecycle.LiveData
import ru.subnak.easybike.domain.model.Journey

interface JourneyRepository {

    suspend fun addJourney(journey: Journey)

    suspend fun deleteJourney(journey: Journey)

    suspend fun getJourney(journeyID: Int): Journey

    fun getJourneyList(): LiveData<List<Journey>>

    fun getJourneyWithJourneyValuesList(): LiveData<List<Journey>>
}