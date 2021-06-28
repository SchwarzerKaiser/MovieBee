package com.leewilson.moviebee.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leewilson.moviebee.model.UserProperties

@Database(
    entities = [UserProperties::class], 
    version = 1, 
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserPropertiesDao(): UserPropertiesDao

    companion object {
        const val DATABASE_NAME = "app_db"
    }
}