package com.leewilson.movienights.persistence

import androidx.room.*
import com.leewilson.movienights.model.UserProperties

@Dao
interface UserPropertiesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAndReplace(userProperties: UserProperties): Long

    @Query("DELETE FROM user_properties")
    fun nullifyOnLogout()

    @Query("SELECT * FROM user_properties WHERE email = :email")
    fun searchByEmail(email: String): UserProperties?
}