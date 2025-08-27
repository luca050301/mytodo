package com.example.mytodo.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface TodoDao {

    @Query("SELECT * FROM todos ORDER BY :sort")
    fun observeAll(sort: String): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE id = :id")
    fun observeById(id: Long): Flow<TodoEntity>

    @Query("SELECT * FROM todos ORDER BY :sort")
    suspend fun getAll(sort: String): List<TodoEntity>

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getById(id: Long): TodoEntity?

    @Query(
        """
        SELECT * FROM todos
        WHERE (:query IS NULL OR :query = "" OR title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%')
        AND (:oneOfLabels IS NULL OR label IN (:oneOfLabels))
        AND (:dateIsAfter IS NULL OR date >= :dateIsAfter)
        AND (:dateIsBefore IS NULL OR date <= :dateIsBefore)
        AND (:hideCompleted = 0 OR isCompleted = 0)
        ORDER BY 
        CASE WHEN :sort = 'default' THEN date END ASC,
        CASE WHEN :sort = 'date ASC' THEN date END DESC,
        CASE WHEN :sort = 'title ASC' THEN title END ASC,
        CASE WHEN :sort = 'createdAt ASC' THEN createdAt END DESC,
        CASE WHEN :sort = 'label ASC' THEN label END ASC
        
    """
    )
    fun observeSearch(
        query: String?,
        oneOfLabels: Set<String>?,
        dateIsAfter: LocalDateTime?,
        dateIsBefore: LocalDateTime?,
        hideCompleted: Boolean?,
        sort: String?
    ): Flow<List<TodoEntity>>

    @Upsert
    suspend fun upsert(todo: TodoEntity)

    @Query("DELETE FROM todos WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM todos WHERE isCompleted = 1") // booleans require API level 30
    suspend fun deleteCompleted()

}