package com.example.mytodo.data

import com.example.mytodo.data.local.TodoDao
import com.example.mytodo.data.model.SearchFilters
import com.example.mytodo.data.model.SortType
import com.example.mytodo.data.model.Todo
import com.example.mytodo.data.remote.TodoApiInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDateTime
import javax.inject.Inject

class DefaultTodoRepository @Inject constructor(
    private val todoDao: TodoDao,
    private val todoApi: TodoApiInterface,
    private val todoMapper: TodoMapper
) : TodoRepository {
    override fun getTodosFlow(sort: SortType): Flow<List<Todo>> {
        return todoDao.observeAll(sort.toString()).map {
            it.map { entity -> todoMapper.entityToModel(entity) }
        }

    }

    override fun getTodoFlowById(id: Long): Flow<Todo?> {
        return todoDao.observeById(id).map { entity -> todoMapper.entityToModel(entity) }
    }

    override fun searchTodos(
        searchFilters: SearchFilters,
        sort: SortType
    ): Flow<List<Todo>> {
        return todoDao.observeSearch(
            searchFilters.query,
            searchFilters.oneOfLabels, searchFilters.dateIsAfter,
            searchFilters.dateIsBefore, searchFilters.hideCompleted, sort.toString()
        ).map {
            it.map { entity -> todoMapper.entityToModel(entity) }
        }
    }

    override suspend fun getTodos(sort: SortType): List<Todo> {
        return todoDao.getAll(sort.toString())
            .map { entity -> todoMapper.entityToModel(entity) }
    }

    override suspend fun getTodoById(id: Long): Todo? {
        return todoDao.getById(id)?.let { entity -> todoMapper.entityToModel(entity) }
    }

    override suspend fun upsertTodo(todo: Todo) {
        todoDao.upsert(todoMapper.modelToEntity(todo))
    }

    override suspend fun deleteTodoById(id: Long) {
        todoDao.deleteById(id)
    }

    override suspend fun deleteCompletedTodos() {
        todoDao.deleteCompleted()
    }

    override suspend fun syncTodos() {
        return
        TODO("Not yet implemented")
    }

    override fun getCurrentLabels(): Flow<List<String>> {
        return todoDao.observeLabels()
    }

    override fun searchCurrentLabels(query: String): Flow<List<String>> {
        return todoDao.observeLabelsSearch(query)
    }

    override suspend fun initDummyData() {
        if (todoDao.getAll(SortType.DEFAULT.toString()).isEmpty()) {
            val createdAt = Instant.now()
            val now = LocalDateTime.now()
            val todos = listOf(
                Todo(
                    id = 1,
                    title = "Buy groceries",
                    date = now.plusDays(1),
                    label = "Personal",
                    description = "Bread, Vegetables, Fruits",
                    createdAt = createdAt,
                    isCompleted = false
                ),
                Todo(
                    id = 2,
                    title = "Finish the todo app :)",
                    date = now.plusDays(2),
                    label = "Work",
                    description = "Complete the repository and UI layers",
                    createdAt = createdAt,
                    isCompleted = false
                ),
                Todo(
                    id = 3,
                    title = "Go for a run",
                    date = now.plusDays(3),
                    label = "Fitness",
                    description = "5km in the park",
                    createdAt = createdAt,
                    isCompleted = false
                ),
            )
            todos.forEach {
                todoDao.upsert(todoMapper.modelToEntity(it))
            }
        }
    }
}