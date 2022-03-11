package com.example.alpha.db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.alpha.db.dao.AppDao
import com.example.alpha.db.entity.Line
import com.example.alpha.db.entity.Party
import com.example.alpha.db.entity.PartyTransaction
import com.example.alpha.db.entity.User
import java.lang.Exception

class AppRepository(){
    private val db : AppDatabase? = AppDatabase.getDatabase();
    private val appDao: AppDao? = db!!.appDao()

    companion object{
        private var appRepository : AppRepository? = null
        fun getAppRepositoryInstance() : AppRepository? {
            if(appRepository == null)
            {
                appRepository = AppRepository()
            }
            return appRepository
        }
    }
    suspend fun getAllParties(): LiveData<List<Party>>
    {
        return appDao!!.getAllParties()
    }
    suspend fun getTransactions(): LiveData<List<PartyTransaction>>
    {
        return appDao!!.getTransactions()
    }
    suspend fun getPartyTransactions(partyId:Int): LiveData<List<PartyTransaction>>
    {
        return appDao!!.getPartyTransactions(partyId)
    }
    suspend fun getLineParties(lineId: Int): LiveData<List<Party>>
    {
        return appDao!!.getLineParties(lineId)
    }
    suspend fun addPartyTransaction(partyTransaction: PartyTransaction, party: Party) : String?
    {
        try {
            appDao?.addPartyTransaction(partyTransaction)
            appDao?.updateParty(party)
        }catch (e : Exception){
            Log.i("Exception", "addPartyTransaction() ${e.message}")
            return if(e.message!!.contains("2067")) {
                "DUPLICATE"
            } else {
                e.message
            }
        }
        return "SUCCESS"
    }
    suspend fun deleteParty(party: Party)
    {
        appDao!!.deleteParty(party)
    }
    suspend fun deleteLine(line: Line)
    {
        appDao!!.deleteLine(line)
    }
    suspend fun getAllLines(): LiveData<List<Line>>
    {
        return appDao!!.getAllLines()
    }
    fun getAllLinesAsList(): List<Line>
    {
        return appDao!!.getAllLinesAsList() as List<Line>
    }
    suspend fun addLine(line: Line) : String?
    {
        try {
            appDao?.addLine(line)
        }catch (e : Exception){
            Log.i("Exception", "addLine() ${e.message}")
            return if(e.message!!.contains("2067")) {
                "DUPLICATE"
            } else {
                e.message
            }
        }
        return "SUCCESS"
    }
    suspend fun updateLine(line: Line) : String?
    {
        try {
            appDao?.updateLine(line)
        }catch (e : Exception){
            Log.i("Exception", "updateLine() ${e.message}")
            return if(e.message!!.contains("2067")) {
                "DUPLICATE"
            } else {
                e.message
            }
        }
        return "SUCCESS"
    }
    suspend fun updateParty(party: Party) : String?
    {
        try {
            appDao?.updateParty(party)
        }catch (e : Exception){
            Log.i("Exception", "addLine() ${e.message}")
            return if(e.message!!.contains("2067")) {
                "DUPLICATE"
            } else {
                e.message
            }
        }
        return "SUCCESS"
    }
    suspend fun deleteAllParties()
    {
        appDao?.deleteAllParties()
    }
    suspend fun deleteAllLines()
    {
        appDao?.deleteAllLines()
    }
    suspend fun addParty(party: Party) : String?
    {
        try {
            appDao?.addParty(party)
        }catch (e : Exception){
            Log.i("Exception", "addLine() ${e.message}")
            return if(e.message!!.contains("2067")) {
                "DUPLICATE"
            } else {
                e.message
            }
        }
        return "SUCCESS"
    }
    fun getLine(lineId : Int) : Line
    {
        return appDao!!.getLine(lineId)
    }
    fun getParty(lineId : Int) : Party
    {
        return appDao!!.getParty(lineId)
    }
    fun getLatestPartyTransactions(partyId:Int): PartyTransaction
    {
        return appDao!!.getLatestPartyTransactions(partyId)
    }
}