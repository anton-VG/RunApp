package ru.gribkov.runapp.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.gribkov.runapp.db.Run
import ru.gribkov.runapp.other.SortType
import ru.gribkov.runapp.repositories.MainRepository
import javax.inject.Inject

class MainViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
): ViewModel() {

    private val runsSortByDate = mainRepository.getAllRunsSortedByDate()
    private val runsSortByDistance = mainRepository.getAllRunsSortedByDistance()
    private val runsSortByCaloriesBurned = mainRepository.getAllRunsSortedByCaloriesBurned()
    private val runsSortByTimeInMillis = mainRepository.getAllRunsSortedByTimeInMillis()
    private val runsSortByAvgSpeed= mainRepository.getAllRunsSortedByAvgSpeed()

    val runs = MediatorLiveData<List<Run>>()

    var sortType = SortType.DATE

    init {
        runs.addSource(runsSortByDate) { result ->
            if (sortType == SortType.DATE) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortByTimeInMillis) { result ->
            if (sortType == SortType.RUNNING_TIME) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortByDistance) { result ->
            if (sortType == SortType.DISTANCE) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortByAvgSpeed) { result ->
            if (sortType == SortType.AVG_SPEED) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortByCaloriesBurned) { result ->
            if (sortType == SortType.CALORIES_BURNED) {
                result?.let { runs.value = it }
            }
        }
    }

    fun sortRuns(sortType: SortType) = when(sortType) {
        SortType.DATE -> runsSortByDate.value?.let { runs.value = it }
        SortType.RUNNING_TIME -> runsSortByTimeInMillis.value?.let { runs.value = it }
        SortType.DISTANCE -> runsSortByDistance.value?.let { runs.value = it }
        SortType.AVG_SPEED -> runsSortByAvgSpeed.value?.let { runs.value = it }
        SortType.CALORIES_BURNED -> runsSortByCaloriesBurned.value?.let { runs.value = it }
    }.also {
        this.sortType = sortType
    }

    fun insertRun(run: Run) = viewModelScope.launch {
        mainRepository.insertRun(run)
    }
}