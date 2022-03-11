package com.example.alpha.ui.users

import androidx.lifecycle.*
import com.example.alpha.db.AppRepository
import com.example.alpha.db.entity.Party
import kotlinx.coroutines.launch

class UsersViewModel : ViewModel() {

    private val appRepository: AppRepository? = AppRepository.getAppRepositoryInstance()
    lateinit var  usersLiveData: LiveData<List<Party>>
    var lineId : Int? = null

    fun start()
    {
        viewModelScope.launch {
            usersLiveData = if(lineId == null) {
                getAllParties()
            } else {
                getLineParties(lineId!!)
            }
        }
    }
    private suspend fun getAllParties() : LiveData<List<Party>>
    {
        return appRepository!!.getAllParties()
    }
    private suspend fun getLineParties(lineId : Int) : LiveData<List<Party>>
    {
        return appRepository!!.getLineParties(lineId)
    }
    suspend fun deleteAllParties()
    {
        appRepository!!.deleteAllParties()
    }
    suspend fun deleteParty(party: Party)
    {
        appRepository!!.deleteParty(party)
    }
    fun getParty(partyId: Int) : Party
    {
        return appRepository!!.getParty(partyId)
    }
}