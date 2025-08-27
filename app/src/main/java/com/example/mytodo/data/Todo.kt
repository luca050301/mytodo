package com.example.mytodo.data

import java.time.Instant
import java.time.LocalDateTime

data class Todo(
    val id: Long? = null,
    val title: String = "",
    val date: LocalDateTime = LocalDateTime.now(),
    val label: String = "",
    val description: String = "",
    val createdAt: Instant = Instant.now(),
    val isCompleted: Boolean = false
)