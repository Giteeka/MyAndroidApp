package com.example.myandroidapp.ui.insertupdate

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myandroidapp.data.RoomDataBaseHelper
import com.example.myandroidapp.data.Todo
import com.example.myandroidapp.ui.base.BaseViewModel
import com.example.myandroidapp.utils.Logg
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

public class InsertUpdateViewModel : BaseViewModel<InsertUpdateNavigator>() {

    var TAG = "InsertUpdateVM"
    public fun add(text: String) {
        Logg.e(TAG, "text : $text")
        val todo = Todo()
        todo.title = text
        setIsLoading(true)
        fbHelper?.insertTodo(todo)
        val subscribe =
            dpHelper?.insert(todo)?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(
                {
                    Log.e(TAG, "on next value : $it")
                    todo._Id = it
                },
                {

                    Log.e(TAG, "Error : " + it.message)
                },
                {
                    Log.e(TAG, ":completed ")
                    getNavigator()?.showMessage("Inserted successfully")
                    getNavigator()?.goBack()
                }
            )


    }


//    class Factory(val dataBaseHelper: RoomDataBaseHelper?) :
//        ViewModelProvider.NewInstanceFactory() {
//
//
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//
//            return InsertUpdateViewModel(dataBaseHelper) as T
//        }
//    }
}