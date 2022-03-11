package com.example.alpha.ui.lines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alpha.db.AppRepository
import com.example.alpha.db.entity.Line
import com.example.alpha.db.entity.User
import kotlinx.coroutines.launch

class LinesViewModel : ViewModel() {

    private val appRepository: AppRepository? = AppRepository.getAppRepositoryInstance()
    lateinit var  usersLiveData: LiveData<List<Line>>
    init {
        viewModelScope.launch {
            usersLiveData = getAllLines()
        }
    }

    private suspend fun getAllLines() : LiveData<List<Line>>
    {
        return appRepository!!.getAllLines()
    }
    suspend fun deleteAll()
    {
        appRepository!!.deleteAllLines()
    }
    suspend fun deleteLine(line: Line)
    {
        appRepository!!.deleteLine(line)
    }
}