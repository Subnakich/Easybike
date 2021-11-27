package ru.subnak.easybike.data.repositoriesimpl

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.subnak.easybike.data.AppDatabase
import ru.subnak.easybike.data.mapper.JourneyValueMapper
import ru.subnak.easybike.domain.model.JourneyValue
import ru.subnak.easybike.domain.repositories.JourneyValueRepository

class JourneyValueRepositoryImpl(
    application: Application
): JourneyValueRepository {

    private val journeyValueListDao = AppDatabase.getInstance(application).journeyValueListDao()
    private val mapper = JourneyValueMapper()


    override suspend fun addJourneyValue(journeyValue: JourneyValue) {
        journeyValueListDao.addJourneyValue(mapper.mapEntityToDbModel(journeyValue))
    }

    override fun getJourneyValueList(journeyID: Int): LiveData<List<JourneyValue>> = Transformations.map(
        journeyValueListDao.getJourneyValueList(journeyID)
    ) {
        mapper.mapListDbModelToListEntity(it)
    }
}
