package com.example.mytodo.data.model

import java.time.LocalDateTime

data class SearchFilters (
    var query: String? = null,
    var oneOfLabels: Set<String>? = null,
    var dateIsAfter: LocalDateTime? = null,
    var dateIsBefore: LocalDateTime? = null,
    var hideCompleted: Boolean = false
)