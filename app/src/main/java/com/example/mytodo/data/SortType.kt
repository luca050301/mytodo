package com.example.mytodo.data

enum class SortType {
    DEFAULT {
        override fun toSqlOrderBy() = "date ASC"
    },
    NAME {
        override fun toSqlOrderBy() = "title ASC"
    },
    DATE{
        override fun toSqlOrderBy() = "date ASC"
    },
    TIME_CREATED{
        override fun toSqlOrderBy() = "createdAt ASC"
    },
    LABEL {
        override fun toSqlOrderBy() = "label ASC"
    };

    abstract fun toSqlOrderBy(): String
}