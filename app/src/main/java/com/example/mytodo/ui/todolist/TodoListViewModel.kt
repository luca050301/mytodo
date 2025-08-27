package com.example.mytodo.ui.todolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodo.data.SearchFilters
import com.example.mytodo.data.SortType
import com.example.mytodo.data.Todo
import com.example.mytodo.data.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel  @Inject constructor(private val repository: TodoRepository) : ViewModel()
{
    private val _searchFilters = MutableStateFlow(SearchFilters())
    val searchFilters: StateFlow<SearchFilters> = _searchFilters

    private val _sort = MutableStateFlow(SortType.DEFAULT)
    val sort: StateFlow<SortType> = _sort

    @OptIn(ExperimentalCoroutinesApi::class)
    val todoItems = combine(searchFilters, sort) { filters, sortType ->
        filters to sortType
    }.flatMapLatest { (filters, sortType) ->
        repository.searchTodos(filters, sortType)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList()
    )

    init {
        viewModelScope.launch {
            repository.initDummyData()
            repository.syncTodos()
        }
    }
    fun toggleCompleted(todo: Todo, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.updateTodo(
                todo.copy(
                    isCompleted = isCompleted
                )
            )
        }
    }
    fun setSearchFilters(filters: SearchFilters) {
        _searchFilters.value = filters
    }
    fun setSort(sort: SortType) {
        _sort.value = sort
    }

    fun deleteTodoById(id: Long) {
        viewModelScope.launch {
            repository.deleteTodoById(id)
        }
    }

}