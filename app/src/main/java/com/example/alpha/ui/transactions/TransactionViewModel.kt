package com.example.alpha.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alpha.db.AppRepository
import com.example.alpha.db.entity.PartyTransaction
import kotlinx.coroutines.launch

class TransactionViewModel : ViewModel() {
    private val appRepository: AppRepository? = AppRepository.getAppRepositoryInstance()
    lateinit var  usersLiveData: LiveData<List<PartyTransaction>>
    var partyId:Int? = null

    fun start()
    {
        viewModelScope.launch {
            usersLiveData = if(partyId == null) {
                getAllTransactions()
            } else {
                getPartyTransaction(partyId!!)
            }
        }
    }
    private suspend fun getAllTransactions() : LiveData<List<PartyTransaction>>
    {
        return appRepository!!.getTransactions()
    }
    private suspend fun getPartyTransaction(lineId : Int) : LiveData<List<PartyTransaction>>
    {
        return appRepository!!.getPartyTransactions(lineId)
    }
    suspend fun deleteAllTransactions()
    {
        appRepository!!.deleteAllParties()
    }
}