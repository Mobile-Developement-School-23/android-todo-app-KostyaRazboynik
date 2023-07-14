package com.konstantinmuzhik.hw1todoapp.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.konstantinmuzhik.hw1todoapp.data.datasource.local.entity.ToDoItemEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for ToDoItems
 *
 * @author Konstantin Kovalev
 *
 */
@Dao
interface ToDoItemDao {

    @Query("SELECT * FROM todo_items ORDER BY changedAt DESC")
    fun getToDoItemsFlow(): Flow<List<ToDoItemEntity>>

    @Query("SELECT * FROM todo_items ORDER BY changedAt DESC")
    fun getToDoItems(): List<ToDoItemEntity>

    @Query("SELECT * FROM todo_items WHERE id = :id")
    fun getToDoItemById(id: String): ToDoItemEntity

    @Update
    suspend fun updateToDoItem(toDoItemEntity: ToDoItemEntity)

    @Query("UPDATE todo_items SET done=:done, changedAt=:time WHERE id=:id")
    suspend fun updateDone(id: String, done: Boolean, time: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createToDoItem(vararg toDoItemEntity: ToDoItemEntity)

    @Delete
    suspend fun deleteToDoItem(toDoItemEntity: ToDoItemEntity)

    @Query("DELETE FROM todo_items")
    suspend fun deleteAllToDoItems()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun mergeToDoItems(toDoItems: List<ToDoItemEntity>)
}