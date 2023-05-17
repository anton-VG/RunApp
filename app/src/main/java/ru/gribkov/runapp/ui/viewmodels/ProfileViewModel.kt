package ru.gribkov.runapp.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.gribkov.runapp.db.Run
import ru.gribkov.runapp.other.SortType
import ru.gribkov.runapp.repositories.MainRepository

class ProfileViewModel @ViewModelInject constructor(
   val mainRepository: MainRepository
): ViewModel() {

    private val runsSortByDate = mainRepository.getAllRunsSortedByDate()

    val runs = MediatorLiveData<List<Run>>()
    private val sortType = SortType.DATE

    init {
        runs.addSource(runsSortByDate) { result ->
            if (sortType == SortType.DATE) {
                result?.let { runs.value = it }
            }
        }
    }

    fun deleteById(id: Int) = viewModelScope.launch {
        mainRepository.deleteById(id)
    }
}