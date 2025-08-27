package com.example.mytodo.data

import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    fun getTodosFlow(sort: SortType = SortType.DEFAULT): Flow<List<Todo>>
    fun getTodoFlowById(id: Long): Flow<Todo?>

    fun searchTodos(searchFilters: SearchFilters = SearchFilters(), sort: SortType= SortType.DEFAULT): Flow<List<Todo>>
    suspend fun getTodos(sort: SortType = SortType.DEFAULT): List<Todo>
    suspend fun getTodoById(id: Long): Todo?

    suspend fun addTodo(todo: Todo)
    suspend fun updateTodo(todo: Todo)
    suspend fun deleteTodoById(id: Long)

    suspend fun deleteCompletedTodos()

    suspend fun syncTodos()
    suspend fun initDummyData()
}