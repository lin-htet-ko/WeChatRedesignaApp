package com.linhteko.shared.utils

import java.util.*

fun getDays(month: Int, year: Int): List<Int> {
    val calendarForDay = Calendar.getInstance()
    calendarForDay.set(Calendar.YEAR, year)
    calendarForDay.set(Calendar.MONTH, month)
    calendarForDay.set(Calendar.DAY_OF_MONTH, 1)

    val days = mutableListOf<Int>()
    while (month == calendarForDay.get(Calendar.MONTH)) {
        days.add(calendarForDay.get(Calendar.DAY_OF_MONTH))
        calendarForDay.add(Calendar.DAY_OF_MONTH, 1)
    }
    return days
}

fun calculateUploadTime(instant: Long): String {
    val today = Date(System.currentTimeMillis())
    val date = Date(instant)
    val differentInstant = today.time - date.time

    val calendar = Calendar.getInstance()

//        val second = TimeUnit.SECONDS.toSeconds(differentInstant)
//        val minute = TimeUnit.MINUTES.toMinutes(differentInstant)
//        val hour = TimeUnit.HOURS.toHours(differentInstant)
    val second = differentInstant/ 1000
    val minute = differentInstant/ (1000 * 60)
    val hour = differentInstant/ (1000 * 60 * 60)


    return if(hour >= 48){
        calendar.time = Date(instant)
        "${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH) + 1}-${
            calendar.get(
                Calendar.YEAR
            )
        }"
    }else if (minute > 60) {
        "$hour hour${if (hour > 1) "s" else ""} ago"
    }else if (second >= 60) {
        "$minute min ago"
    }else{
        "a moment ago"
    }

}