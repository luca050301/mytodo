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

    /**
     * Search to-dos by a substring query on title and description, label,
     * start date, end date and completed status
     * Sort by date, title, created time or label
     */
    @Query(
        """
        SELECT * FROM todos
        WHERE (:query IS NULL OR :query = "" OR title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%')
        AND (:oneOfLabels IS NULL OR label IN (:oneOfLabels))
        AND (:dateIsAfter IS NULL OR date >= :dateIsAfter)
        AND (:dateIsBefore IS NULL OR date <= :dateIsBefore)
        AND (:hideCompleted = 0 OR isCompleted = 0)
        ORDER BY 
        CASE WHEN :sort = 'DEFAULT' THEN date END ASC,
        CASE WHEN :sort = 'DATE' THEN date END ASC,
        CASE WHEN :sort = 'NAME' THEN title COLLATE NOCASE END ASC,
        CASE WHEN :sort = 'CREATED_AT' THEN createdAt END DESC,
        CASE WHEN :sort = 'LABEL' THEN label END COLLATE NOCASE ASC
        
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

    /**
     * Get a flow of all currently used labels
     */
    @Query("SELECT DISTINCT label FROM todos WHERE label IS NOT NULL AND label != ''")
    fun observeLabels(): Flow<List<String>>

    /**
     * Search the currently available labels that contain a query string
     */
    @Query("SELECT DISTINCT label FROM todos WHERE label IS NOT NULL AND label != '' AND label LIKE '%' || :query || '%'")
    fun observeLabelsSearch(query: String): Flow<List<String>>

}