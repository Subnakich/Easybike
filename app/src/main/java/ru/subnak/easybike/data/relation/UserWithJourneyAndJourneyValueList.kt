package ru.subnak.easybike.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import ru.subnak.easybike.data.entity.UserDbModel

data class UserWithJourneyAndJourneyValueList(
    @Embedded
    val userDbModel: UserDbModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"
    )
    val journey: List<JourneyWithJourneyValueList>
)
