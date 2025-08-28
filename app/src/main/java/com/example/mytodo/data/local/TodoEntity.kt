package com.example.mytodo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDateTime

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val date: LocalDateTime,
    val label: String?,
    val description: String?,
    val createdAt: Instant = Instant.now(),
    val isCompleted: Boolean = false
)