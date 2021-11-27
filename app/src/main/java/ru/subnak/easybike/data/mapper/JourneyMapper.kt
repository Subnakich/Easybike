package ru.subnak.easybike.data.mapper

import ru.subnak.easybike.data.entity.JourneyDbModel
import ru.subnak.easybike.data.entity.JourneyValueDbModel
import ru.subnak.easybike.data.relation.JourneyWithJourneyValueList
import ru.subnak.easybike.domain.model.Journey
import ru.subnak.easybike.domain.model.JourneyValue

class JourneyMapper {

    fun mapEntityToDbModel(journey: Journey) = JourneyDbModel(
        journeyID = journey.journeyID,
        userID = journey.userID,
        date = journey.date,
        speed = journey.speed,
        distance = journey.distance,
        duration = journey.duration,
        img = journey.img
    )

    fun mapDbModelToEntity(journeyDbModel: JourneyDbModel): Journey {
        val emptyList: List<JourneyValue> = emptyList()
        return Journey(
            journeyID = journeyDbModel.journeyID,
            userID = journeyDbModel.userID,
            date = journeyDbModel.date,
            speed = journeyDbModel.speed,
            distance = journeyDbModel.distance,
            duration = journeyDbModel.duration,
            img = journeyDbModel.img,
            journeyValues = emptyList
        )
    }

    fun mapDbModelWithValuesToEntity(journeyList: JourneyWithJourneyValueList) = Journey(
        journeyID = journeyList.journeyDbModel.journeyID,
        userID = journeyList.journeyDbModel.userID,
        date = journeyList.journeyDbModel.date,
        speed = journeyList.journeyDbModel.speed,
        distance = journeyList.journeyDbModel.distance,
        duration = journeyList.journeyDbModel.duration,
        img = journeyList.journeyDbModel.img,
        journeyValues = mapListDbModelJourneyValueToListEntity(journeyList.journeyValueDbModels)
    )

    fun mapDbModelJourneyValueToEntity(journeyValueDbModel: JourneyValueDbModel) = JourneyValue(
        valueID = journeyValueDbModel.valueID,
        journeyID = journeyValueDbModel.journeyID,
        latitude = journeyValueDbModel.latitude,
        longitude = journeyValueDbModel.longitude,
        altitude = journeyValueDbModel.altitude,
        speed = journeyValueDbModel.speed,
        time = journeyValueDbModel.time,
        systemTime = journeyValueDbModel.systemTime,
        accuracy = journeyValueDbModel.accuracy
    )

    fun mapListDbModelJourneyValueToListEntity(list: List<JourneyValueDbModel>) = list.map {
        mapDbModelJourneyValueToEntity(it)
    }

    fun mapListDbModelToListEntity(list: List<JourneyDbModel>) = list.map {
        mapDbModelToEntity(it)
    }

    fun mapListDbModelWithValuesToListEntity(list: List<JourneyWithJourneyValueList>) = list.map {
        mapDbModelWithValuesToEntity(it)
    }
}