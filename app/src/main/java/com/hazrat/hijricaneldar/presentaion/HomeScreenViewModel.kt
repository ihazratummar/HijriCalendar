package com.hazrat.hijricaneldar.presentaion

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.hijricaneldar.data.entity.GregorianToHijriEntity
import com.hazrat.hijricaneldar.data.entity.HijriCalendarEntity
import com.hazrat.hijricaneldar.domain.repository.GregorianToHijriRepository
import com.hazrat.hijricaneldar.domain.repository.HijriCalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: GregorianToHijriRepository,
    private val hijriCalendarRepository: HijriCalendarRepository
) : ViewModel() {

    private val _hijriDate = MutableStateFlow<List<GregorianToHijriEntity>>(emptyList())
    val hijriDate = _hijriDate.asStateFlow()

    private val _hijriCalendar = MutableStateFlow<List<HijriCalendarEntity>>(emptyList())
    val hijriCalendar = _hijriCalendar.asStateFlow()

    private fun fetchHijriDate() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.gregorianToHijriEntity().distinctUntilChanged()
                .collectLatest { hijriDay: List<GregorianToHijriEntity> ->
                    if (hijriDay.isEmpty()){
                        Log.d("testing",": Empty list ")
                    }else{
                        _hijriDate.value = hijriDay
                    }
                }
        }
    }

    private fun fetchHijriCalendar(){
        viewModelScope.launch(Dispatchers.IO) {
            hijriCalendarRepository.getCalendarList().distinctUntilChanged()
                .collectLatest {calenderList: List<HijriCalendarEntity> ->
                    if (calenderList.isEmpty()){
                        Log.d("testing",": Empty list ")
                    }else{
                        _hijriCalendar.value = calenderList
                    }
                }
        }
    }

    init {
        viewModelScope.launch {
            repository.getGregorianToHijriDate()
            hijriCalendarRepository.getHijriCalendarFromApi()
        }
        fetchHijriDate()
        fetchHijriCalendar()

    }

}