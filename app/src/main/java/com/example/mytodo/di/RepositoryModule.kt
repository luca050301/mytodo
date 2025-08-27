package com.example.mytodo.di

import com.example.mytodo.data.DefaultTodoRepository
import com.example.mytodo.data.TodoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindTodoRepository(impl: DefaultTodoRepository): TodoRepository
}