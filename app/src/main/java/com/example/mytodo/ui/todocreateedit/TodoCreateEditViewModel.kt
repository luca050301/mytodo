package com.example.mytodo.ui.todocreateedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodo.data.Todo
import com.example.mytodo.data.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoCreateEditViewModel @Inject constructor(
    private val repository: TodoRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val todoId: Long? = savedStateHandle["todoId"]
    val isEdit = todoId != null
    val screenTitle: String
        get() = if (isEdit) "Edit ToDo" else "Create ToDo"
    private val _todo: MutableStateFlow<Todo> = MutableStateFlow<Todo>(Todo())

    init {
        if (todoId != null) {
            viewModelScope.launch {
                repository.getTodoById(todoId).let {
                    if (it != null) {
                        _todo.value = it
                    }
                }
            }
        }
    }

    val todo = _todo.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Todo()
    )

    fun updateTodo(newTodo: Todo) {
        _todo.value = newTodo
    }

    fun save(onSaved: () -> Unit) {
        viewModelScope.launch {
            repository.updateTodo(_todo.value)
            onSaved()
        }
    }

}