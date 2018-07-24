package com.zoeyjuan.movetracker.ui.util

import java.text.SimpleDateFormat
import java.util.*

class DateTimeMapper {
    fun map(time: Long): String = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z", Locale.ENGLISH).format(time)
}