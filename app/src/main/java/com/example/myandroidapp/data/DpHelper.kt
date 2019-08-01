package com.example.myandroidapp.data

import com.example.myandroidapp.data.Todo
import io.reactivex.Observable

interface DpHelper {
    fun insert(todo: Todo): Observable<Long>
    fun getTodos(): Observable<List<Todo>>
}