package com.example.mytodo.data

import com.example.mytodo.data.model.SearchFilters
import com.example.mytodo.data.model.SortType
import com.example.mytodo.data.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    fun getTodosFlow(sort: SortType = SortType.DEFAULT): Flow<List<Todo>>
    fun getTodoFlowById(id: Long): Flow<Todo?>

    fun searchTodos(
        searchFilters: SearchFilters = SearchFilters(),
        sort: SortType = SortType.DEFAULT
    ): Flow<List<Todo>>

    suspend fun getTodos(sort: SortType = SortType.DEFAULT): List<Todo>
    suspend fun getTodoById(id: Long): Todo?

    suspend fun upsertTodo(todo: Todo)
    suspend fun deleteTodoById(id: Long)

    suspend fun deleteCompletedTodos()

    suspend fun syncTodos()

    fun getCurrentLabels(): Flow<List<String>>
    fun searchCurrentLabels(query: String): Flow<List<String>>
    suspend fun initDummyData()
}