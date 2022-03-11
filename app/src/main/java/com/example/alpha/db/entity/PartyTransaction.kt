package com.example.alpha.db.entity

import androidx.room.*

@Entity(indices = [Index(
            value = ["party_id"]
    )]
)
data class PartyTransaction(
        @PrimaryKey(autoGenerate = true)
        var transaction_id: Int?,

        @ForeignKey(
                entity = Party::class,
                parentColumns = ["party_id"],
                childColumns = ["party_id"],
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        )
        var party_id: Int,

        @ColumnInfo(name = "time_in_millis")
        var time_in_millis: Long,

        @ColumnInfo(name = "amount_paid")
        var amount_paid: Long,

        @ColumnInfo(name = "remaining_amount")
        var remaining_amount: Long

)