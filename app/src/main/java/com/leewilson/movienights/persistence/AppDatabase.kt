package com.leewilson.movienights.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leewilson.movienights.model.UserProperties

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