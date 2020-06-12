package com.leewilson.movienights.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey

@Entity(tableName = "user_properties")
data class UserProperties(

    @ColumnInfo(name = "pk")
    @PrimaryKey(autoGenerate = false)
    val pk: Int,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "password")
    val password: String
)