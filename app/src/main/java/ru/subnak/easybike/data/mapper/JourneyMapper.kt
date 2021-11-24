package ru.subnak.easybike.data.mapper

import ru.subnak.easybike.data.entity.JourneyDbModel
import ru.subnak.easybike.domain.journey.Journey

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

    fun mapDbModelToEntity(journeyDbModel: JourneyDbModel) = Journey(
        journeyID = journeyDbModel.journeyID,
        userID = journeyDbModel.userID,
        date = journeyDbModel.date,
        speed = journeyDbModel.speed,
        distance = journeyDbModel.distance,
        duration = journeyDbModel.duration,
        img = journeyDbModel.img
    )

    fun mapListDbModelToListEntity(list: List<JourneyDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}