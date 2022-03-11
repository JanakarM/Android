package com.example.alpha.db.entity

import androidx.room.*
import java.io.Serializable

@Entity(indices = [Index(
        value = ["party_name"],
        unique = true
),
        Index(
                value = ["line_id"]
        )]
)
class Party(
        @PrimaryKey(autoGenerate = true)
        var party_id: Int?,

        @ColumnInfo(name = "party_name")
        var party_name: String,

        @ForeignKey(
                entity = Line::class,
                parentColumns = ["line_id"],
                childColumns = ["line_id"],
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        )
        var line_id: Int,

        @ColumnInfo(name = "amount_to_be_paid")
        var amount_to_be_paid: Long,

        @ColumnInfo(name = "total_amount")
        var total_amount: Long,

        @ColumnInfo(name = "interest")
        var interest: Long?,

        @ColumnInfo(name = "date_in_millis")
        var date_in_millis: Long = System.currentTimeMillis(),

        @ColumnInfo(name = "active")
        var active: Boolean = true,

        @ColumnInfo(name = "party_type")
        var party_type: String,
):Serializable