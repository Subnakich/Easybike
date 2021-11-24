package ru.subnak.easybike.data.repositoryImpl

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.subnak.easybike.data.AppDatabase
import ru.subnak.easybike.data.mapper.JourneyMapper
import ru.subnak.easybike.domain.journey.Journey
import ru.subnak.easybike.domain.journey.JourneyRepository

class JourneyRepositoryImpl(
    application: Application
): JourneyRepository {

    private val journeyListDao = AppDatabase.getInstance(application).journeyListDao()
    private val mapper = JourneyMapper()

    override suspend fun addJourney(journey: Journey) {
        journeyListDao.addJourney(mapper.mapEntityToDbModel(journey))
    }

    override suspend fun deleteJourney(journey: Journey) {
        journeyListDao.deleteJourney(journey.journeyID)
    }

    override suspend fun editJourney(journey: Journey) {
        journeyListDao.addJourney(mapper.mapEntityToDbModel(journey))
    }

    override suspend fun getJourney(journeyID: Int): Journey {
        val dbModel = journeyListDao.getJourney(journeyID)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override fun getJourneyList(): LiveData<List<Journey>> = Transformations.map(
        journeyListDao.getJourneyList()
    ) {
        mapper.mapListDbModelToListEntity(it)
    }
}