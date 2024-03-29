package ru.subnak.easybike.data.repositoriesimpl

import android.app.Application
import ru.subnak.easybike.data.AppDatabase
import ru.subnak.easybike.data.mapper.UserMapper
import ru.subnak.easybike.domain.model.User
import ru.subnak.easybike.domain.repositories.UserRepository

class UserRepositoryImpl(
    application: Application
) : UserRepository {

    private val userDao = AppDatabase.getInstance(application).userDao()
    private val mapper = UserMapper()

    override suspend fun addUser(user: User) {
        userDao.addUser(mapper.mapEntityToDbModel(user))
    }

    override suspend fun editUser(user: User) {
        userDao.addUser(mapper.mapEntityToDbModel(user))
    }

    override suspend fun getUser(userID: Int): User {
        val dbModel = userDao.getUser(userID)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override suspend fun checkUser(userID: Int): Int {
        return userDao.checkUser(userID)
    }
}