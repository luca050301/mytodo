package com.example.mytodo.data.remote

import java.time.Instant
import java.time.LocalDateTime

data class TodoDto(
    val id: Long?,
    val title: String,
    val date: LocalDateTime,
    val label: String,
    val description: String,
    val createdAt: Instant = Instant.now(),
    val isCompleted: Boolean = false
)