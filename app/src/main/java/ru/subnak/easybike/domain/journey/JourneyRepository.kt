package ru.subnak.easybike.domain.journey

import androidx.lifecycle.LiveData

interface JourneyRepository {

    suspend fun addJourney(journey: Journey)

    suspend fun deleteJourney(journey: Journey)

    suspend fun getJourney(journeyID: Int): Journey

    fun getJourneyList(): LiveData<List<Journey>>

    fun getJourneyWithJourneyValuesList(): LiveData<List<Journey>>
}