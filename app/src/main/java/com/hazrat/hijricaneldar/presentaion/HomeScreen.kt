package com.hazrat.hijricaneldar.presentaion

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hazrat.hijricaneldar.R
import com.hazrat.hijricaneldar.data.entity.HijriCalendarEntity

@Composable
fun HomeScreen() {
    CalendarScreen()
}

@Composable
fun CalendarScreen(viewModel: HomeScreenViewModel = hiltViewModel()) {
    val hijriCalendar by viewModel.hijriCalendar.collectAsState()
    val date = hijriCalendar.firstOrNull()


    val weekNames = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val daysByWeekday = hijriCalendar.chunked(7)



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Hijri Calendar", style = MaterialTheme.typography.headlineSmall)
        WeekNamesRow()
        daysByWeekday.forEachIndexed { index, week ->
            WeekItem(week = week, weekNames = weekNames, isFirstWeek = index == 0)
        }
    }
}

@Composable
fun WeekNamesRow() {
    val weekNames = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    Row(
        modifier = Modifier.fillMaxWidth(),
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

@Composable
fun WeekItem(week: List<HijriCalendarEntity>, weekNames: List<String>, isFirstWeek: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Add a divider for every week after the first one
        if (!isFirstWeek) {
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            // Display each day in the week
            week.forEachIndexed { index, day ->
                DayItem(day = day, weekNames[index])
            }
        }
    }
}

@Composable
fun DayItem(day: HijriCalendarEntity, s: String) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .clickable { /* Handle click event */ }
            .background(Color.LightGray)
            .size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = day.hijriDay.toString())
            Text(text = day.gregorianDay.toString(), fontSize = 10.sp)
        }
    }
}