package com.example.myandroidapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Todo::class), version = 1)
abstract class RoomDataBaseHelper : RoomDatabase() {

    abstract fun todoDao(): TodoDao?

}