package com.example.mytodo.data

import com.example.mytodo.data.local.TodoEntity
import com.example.mytodo.data.model.Todo
import com.example.mytodo.data.remote.TodoDto
import javax.inject.Inject

class TodoMapper @Inject constructor() {
    fun entityToModel(todoEntity: TodoEntity): Todo = Todo(
        id = todoEntity.id,
        title = todoEntity.title,
        date = todoEntity.date,
        label = todoEntity.label,
        description = todoEntity.description,
        createdAt = todoEntity.createdAt,
        isCompleted = todoEntity.isCompleted
    )

    fun modelToEntity(todo: Todo): TodoEntity = TodoEntity(
        id = todo.id ?: 0,
        title = todo.title,
        date = todo.date,
        label = todo.label,
        description = todo.description,
        createdAt = todo.createdAt,
        isCompleted = todo.isCompleted
    )

    fun dtoToModel(todoDto: TodoDto): Todo = Todo(
        id = todoDto.id,
        title = todoDto.title,
        date = todoDto.date,
        label = todoDto.label,
        description = todoDto.description,
        createdAt = todoDto.createdAt,
        isCompleted = todoDto.isCompleted
    )

    fun modelToDto(todo: Todo): TodoDto = TodoDto(
        id = todo.id,
        title = todo.title,
        date = todo.date,
        label = todo.label,
        description = todo.description,
        createdAt = todo.createdAt,
        isCompleted = todo.isCompleted
    )

}