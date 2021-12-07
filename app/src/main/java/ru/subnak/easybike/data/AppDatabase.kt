package ru.subnak.easybike.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.subnak.easybike.data.dao.JourneyListDao
import ru.subnak.easybike.data.dao.JourneyValueListDao
import ru.subnak.easybike.data.dao.UserDao
import ru.subnak.easybike.data.entity.JourneyDbModel
import ru.subnak.easybike.data.entity.JourneyValueDbModel
import ru.subnak.easybike.data.entity.UserDbModel

@Database(
    entities = [UserDbModel::class, JourneyDbModel::class, JourneyValueDbModel::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun journeyListDao(): JourneyListDao
    abstract fun journeyValueListDao(): JourneyValueListDao

    companion object {

        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "easy_bike.db"

        fun getInstance(application: Application): AppDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = db
                return db
            }
        }
    }
}
