package com.example.myandroidapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Todo")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    var _Id: Long? = null,

    @ColumnInfo(name = "title")
    var title: String? = ""
){
    override fun toString(): String {
        return "--> $_Id || $title"
    }
}