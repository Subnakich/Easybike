package ru.subnak.easybike.data.mapper

import ru.subnak.easybike.data.entity.UserDbModel
import ru.subnak.easybike.domain.user.User

class UserMapper {

    fun mapEntityToDbModel(user: User) = UserDbModel(
        userID = user.userID,
        name = user.name,
        age = user.age,
        weight = user.weight,
        height = user.height,
        sex = user.sex
    )

    fun mapDbModelToEntity(userDbModel: UserDbModel) = User(
        userID = userDbModel.userID,
        name = userDbModel.name,
        age = userDbModel.age,
        weight = userDbModel.weight,
        height = userDbModel.height,
        sex = userDbModel.sex
    )
}