package ru.subnak.easybike.data.mapper

import ru.subnak.easybike.data.entity.JourneyValueDbModel
import ru.subnak.easybike.domain.model.JourneyValue

class JourneyValueMapper {

    fun mapEntityToDbModel(journeyValue: JourneyValue) = JourneyValueDbModel(
        valueID = journeyValue.valueID,
        journeyID = journeyValue.journeyID,
        latitude = journeyValue.latitude,
        longitude = journeyValue.longitude,
        altitude = journeyValue.altitude,
        speed = journeyValue.speed,
        time = journeyValue.time,
        systemTime = journeyValue.systemTime,
        accuracy = journeyValue.accuracy
    )

    fun mapDbModelToEntity(journeyValueDbModel: JourneyValueDbModel) = JourneyValue(
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

    fun mapListDbModelToListEntity(list: List<JourneyValueDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}