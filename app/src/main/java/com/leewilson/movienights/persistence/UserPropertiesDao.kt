package com.leewilson.movienights.persistence

import androidx.room.*
import com.leewilson.movienights.model.UserProperties

@Dao
interface UserPropertiesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAndReplace(userProperties: UserProperties): Long

    @Delete
    fun nullifyOnLogout(userProperties: UserProperties)

    @Query("SELECT * FROM user_properties WHERE email = :email")
    fun searchByEmail(email: String): UserProperties?

    @Update
    fun updateUserProperties(userProperties: UserProperties)
}