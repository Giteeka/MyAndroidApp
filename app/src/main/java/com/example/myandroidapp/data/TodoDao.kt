package com.example.myandroidapp.data


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDao {

    @Query("SELECT * from Todo")
    fun getAll(): List<Todo>

    @Insert(onConflict = REPLACE)
    fun insert(todo: Todo): Long

    @Update(onConflict = REPLACE)
    fun update(todo: Todo)

    @Query("DELETE from Todo")
    fun deleteAll()

}
