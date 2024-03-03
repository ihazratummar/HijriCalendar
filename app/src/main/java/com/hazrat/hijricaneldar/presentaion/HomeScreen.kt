package com.hazrat.hijricaneldar.presentaion

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hazrat.hijricaneldar.data.entity.HijriCalendarEntity
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen() {
    CalendarScreen()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(viewModel: HomeScreenViewModel = hiltViewModel()) {
    val hijriCalendar by viewModel.hijriCalendar.collectAsState()
    val calendar = hijriCalendar.firstOrNull()

    val startingDay = hijriCalendar.firstOrNull()?.gregorianWeekDayEn
    val weekNames = startingDay?.let { getWeekNames(it) } ?: listOf()

    val firstDayOfMonthIndex = weekNames.indexOf(startingDay)

    val daysWithOffset = mutableListOf<HijriCalendarEntity?>()
    if (firstDayOfMonthIndex != -1) {
        repeat(firstDayOfMonthIndex) {
            daysWithOffset.add(null)
        }
    }
    daysWithOffset.addAll(hijriCalendar)

    val selectedDay = remember { mutableStateOf<HijriCalendarEntity?>(null) }

    val paddedDays = if (daysWithOffset.size % 7 != 0) {
        val remainingDays = 7 - (daysWithOffset.size % 7)
        repeat(remainingDays) {
            daysWithOffset.add(null)
        }
        daysWithOffset.chunked(7)
    } else {
        daysWithOffset.chunked(7)
    }

    val hijriDay by viewModel.hijriDate.collectAsState()
    val hijriDayNew = hijriDay.firstOrNull()
    if (hijriDayNew != null) {
        Log.d("HomeScreen" ,"${hijriDayNew.day}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hijri Calendar", style = MaterialTheme.typography.headlineSmall)
        if (calendar != null) {
            if (hijriDayNew != null) {
                Text(text = "${hijriDayNew.day} ${hijriDayNew.monthEn} ${hijriDayNew.year}${calendar.hijriAbbreviated}")
            }
        }
        WeekNamesRow(weekNames)
        paddedDays.forEach { week ->
            WeekItem(
                week = week.filterNotNull(),
                selectedDay = selectedDay
            )
        }
    }
}

fun getWeekNames(startingDay: String): List<String> {
    val days = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    val startIndex = days.indexOf(startingDay)
    val adjustedWeekNames = mutableListOf<String>()
    if (startIndex != -1) {
        for (i in 0 until 7) {
            adjustedWeekNames.add(days[(startIndex + i) % 7].take(3))
        }
    }
    return adjustedWeekNames
}

@Composable
fun WeekNamesRow(weekNames: List<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        weekNames.forEach { weekName ->
            Text(
                text = weekName,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekItem(
    week: List<HijriCalendarEntity>,
    selectedDay: MutableState<HijriCalendarEntity?>
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        week.forEach { day ->
            DayItem(
                day = day,
                selectedDay = selectedDay
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayItem(
    day: HijriCalendarEntity,
    selectedDay: MutableState<HijriCalendarEntity?>
) {
    val currentDay = LocalDate.now().dayOfMonth
    val backgroundColor = if (selectedDay.value == day && day.gregorianDay != currentDay) {
        Color.Blue.copy(0.1f)
    } else {
        if (day.gregorianDay == currentDay) {
            Color.Cyan.copy(0.1f)
        } else {
            Color.Transparent
        }
    }
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 6.dp)
            .width(40.dp)
            .clickable {
                selectedDay.value = day
            }
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = day.hijriDay.toString())
            Text(text = day.gregorianDay.toString(), fontSize = 10.sp)
            if (currentDay == day.gregorianDay){
                Box (
                    modifier = Modifier
                        .size(6.dp)
                        .background(Color.Cyan, shape = CircleShape)
                        .padding(4.dp)
                ){

                }
            }
        }
    }
}