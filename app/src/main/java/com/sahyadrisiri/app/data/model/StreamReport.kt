package com.sahyadrisiri.app.data.model

data class StreamReport(
    val clarity: Int = 0,
    val flow: String = "",
    val pollution: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis()
)