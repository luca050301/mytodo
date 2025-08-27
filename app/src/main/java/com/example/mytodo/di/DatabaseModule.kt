package com.example.mytodo.di

import android.content.Context
import androidx.room.Room
import com.example.mytodo.data.local.TodoDatabase
import com.example.mytodo.data.remote.TodoApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideBaseUrl(): String = "https://lucam.tech"


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : TodoDatabase {
        return Room.databaseBuilder(
            context,
            TodoDatabase::class.java,
            "todo_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTodoDao(db: TodoDatabase) = db.todoDao()

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideTodoApi(retrofit: Retrofit) = retrofit.create(TodoApiInterface::class.java)
}