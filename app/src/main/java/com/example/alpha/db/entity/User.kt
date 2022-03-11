package com.example.alpha.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(
    value = ["user_name"],
    unique = true
)]
)
data class User(
    @PrimaryKey(autoGenerate = true)
    var user_id: Int?,

    @ColumnInfo(name = "user_name")
    var user_name: String?,

    @ColumnInfo(name = "mobile")
    var mobile: String?,

    @ColumnInfo(name = "email")
    var email: String?
)