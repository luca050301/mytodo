package com.example.mytodo.data.model

enum class SortType(val asUIString: String) {
    DEFAULT("Default"),
    NAME("Name"),
    DATE("Date"),
    CREATED_AT("Time created"),
    LABEL("Label");

    companion object{
        fun from(asUIString: String): SortType = SortType.entries.first { it.asUIString == asUIString }

    }

}