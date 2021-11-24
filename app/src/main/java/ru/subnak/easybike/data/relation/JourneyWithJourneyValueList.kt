package ru.subnak.easybike.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import ru.subnak.easybike.data.entity.JourneyDbModel
import ru.subnak.easybike.data.entity.JourneyValueDbModel

data class JourneyWithJourneyValueList(
    @Embedded
    val journeyDbModel: JourneyDbModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "journey_id"
    )
    val journeyValueDbModels: List<JourneyValueDbModel>
)

