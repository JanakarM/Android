package com.example.alpha.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.DayOfWeek

@Entity(indices = [Index(
    value = ["line_name"],
    unique = true
),
    Index(
            value = ["place"],
            unique = true
    )]
)
class Line(@ColumnInfo(name = "line_name") var line_name: String, @ColumnInfo(name = "place") var place: String,
           @ColumnInfo(name = "day_of_week") var dayOfWeek: DayOfWeek,
           @ColumnInfo(name = "active") var active: Boolean = true,
           @PrimaryKey(autoGenerate = true) var line_id: Int = 0) : Serializable