package com.example.alpha.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.alpha.db.entity.Line
import com.example.alpha.db.entity.Party
import com.example.alpha.db.entity.PartyTransaction
import com.example.alpha.db.entity.User

@Dao
interface AppDao{
    @Query("SELECT * FROM user")
    fun getAllUsers(): LiveData<List<User>>

    @Insert
    suspend fun addUser(vararg users: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM user")
    suspend fun deleteAllUsers();

    @Query("SELECT * FROM line")
    fun getAllLines(): LiveData<List<Line>>

    @Query("SELECT * FROM line")
    fun getAllLinesAsList(): List<Line>

    @Insert
    suspend fun addLine(vararg line: Line)

    @Update
    suspend fun updateLine(vararg line: Line)

    @Update
    suspend fun updateParty(vararg party: Party)

    @Delete
    suspend fun deleteLine(line: Line)

    @Query("DELETE FROM line")
    suspend fun deleteAllLines();

    @Query("SELECT * FROM party")
    fun getAllParties(): LiveData<List<Party>>

    @Insert
    suspend fun addParty(vararg party: Party)

    @Delete
    suspend fun deleteParty(party: Party)

    @Query("DELETE FROM party")
    suspend fun deleteAllParties();

    @Query("Select * from party where party_id=:partyId")
    fun getParty(partyId: Int) : Party;

    @Query("Select * from line where line_id=:lineId")
    fun getLine(lineId: Int) : Line;

    @Query("Select * from party where line_id=:lineId")
    fun getLineParties(lineId: Int) : LiveData<List<Party>>

    @Query("Select * from partytransaction")
    fun getTransactions() : LiveData<List<PartyTransaction>>

    @Query("Select * from partytransaction where party_id=:partyId order by time_in_millis desc")
    fun getPartyTransactions(partyId: Int) : LiveData<List<PartyTransaction>>

    @Query("Select * from partytransaction where party_id=:partyId order by time_in_millis desc limit 1")
    fun getLatestPartyTransactions(partyId: Int) : PartyTransaction

    @Insert
    fun addPartyTransaction(partyTransaction: PartyTransaction)
}