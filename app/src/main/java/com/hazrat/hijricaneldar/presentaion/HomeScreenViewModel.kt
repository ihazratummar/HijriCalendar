package com.hazrat.hijricaneldar.presentaion

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.hijricaneldar.data.entity.GregorianToHijriEntity
import com.hazrat.hijricaneldar.data.entity.HijriCalendarEntity
import com.hazrat.hijricaneldar.domain.model.Task
import com.hazrat.hijricaneldar.domain.repository.GregorianToHijriRepository
import com.hazrat.hijricaneldar.domain.repository.HijriCalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: GregorianToHijriRepository,
    private val hijriCalendarRepository: HijriCalendarRepository
) : ViewModel() {

    private val _hijriDate = MutableStateFlow<List<GregorianToHijriEntity>>(emptyList())
    val hijriDate = _hijriDate.asStateFlow()

    private val _hijriCalendar = MutableStateFlow<List<HijriCalendarEntity>>(emptyList())
    val hijriCalendar = _hijriCalendar.asStateFlow()

    // State to hold the selected day
    private val _selectedDay = mutableStateOf<LocalDate?>(null)
    val selectedDay: State<LocalDate?> = _selectedDay

    // State to hold the tasks for each day
    private val tasksByDay = mutableStateMapOf<LocalDate, List<Task>>()

    private fun fetchHijriDate() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.gregorianToHijriEntity().distinctUntilChanged()
                .collectLatest { hijriDay: List<GregorianToHijriEntity> ->
                    if (hijriDay.isEmpty()) {
                        Log.d("testing", ": Empty list ")
                    } else {
                        _hijriDate.value = hijriDay
                    }
                }
        }
    }

    private fun fetchHijriCalendar() {
        viewModelScope.launch(Dispatchers.IO) {
            hijriCalendarRepository.getCalendarList().distinctUntilChanged()
                .collectLatest { calenderList: List<HijriCalendarEntity> ->
                    if (calenderList.isEmpty()) {
                        Log.d("testing", ": Empty list ")
                    } else {
                        _hijriCalendar.value = calenderList
                    }
                }
        }
    }

    init {
        /// Initialize tasks for the current day
        _selectedDay.value = LocalDate.now()
        val initialTasks = listOf(
            Task("Task 1", isChecked = false),
            Task("Task 2", isChecked = false),
            Task("Task 3", isChecked = false),
            Task("Task 4", isChecked = false),
            Task("Task 5", isChecked = false)
        )
        tasksByDay[LocalDate.now()] = initialTasks

        viewModelScope.launch {
            repository.getGregorianToHijriDate()
            hijriCalendarRepository.getHijriCalendarFromApi()
        }
        fetchHijriDate()
        fetchHijriCalendar()

    }

    // Function to update the selected day
    fun updateSelectedDay(day: LocalDate) {
        _selectedDay.value = day
    }

    // Function to get tasks for the selected day
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTasksForSelectedDay(): List<Task> {
        return tasksByDay[selectedDay.value] ?: emptyList()
    }

    // Function to toggle the checked state of a task
    fun toggleTaskCheckedState(task: Task, isChecked: Boolean) {
        val tasks = tasksByDay[selectedDay.value]?.toMutableList() ?: return
        val index = tasks.indexOf(task)
        if (index != -1) {
            val updatedTask = task.copy(isChecked = isChecked)
            tasks[index] = updatedTask
            selectedDay.value?.let { day ->
                tasksByDay[day] = tasks
            }
        }
    }
}