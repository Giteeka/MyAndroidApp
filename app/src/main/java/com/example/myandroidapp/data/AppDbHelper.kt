package com.example.myandroidapp.data

import io.reactivex.Observable

class AppDbHelper(val appDatabase: RoomDataBaseHelper?) : DpHelper {


    override fun insert(todo: Todo): Observable<Long> {
        return Observable.fromCallable {
            return@fromCallable appDatabase?.todoDao()?.insert(todo)
        }
    }

    override fun getTodos(): Observable<List<Todo>> {
        return Observable.fromCallable {
            appDatabase?.todoDao()?.getAll()
        }
    }
}