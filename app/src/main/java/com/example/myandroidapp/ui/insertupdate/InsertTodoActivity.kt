package com.example.myandroidapp.ui.insertupdate

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.example.myandroidapp.BR
import com.example.myandroidapp.R
import com.example.myandroidapp.data.RoomDataBaseHelper
import com.example.myandroidapp.ui.base.BaseActivityMvvm
import javax.inject.Inject

class InsertTodoActivity :
    BaseActivityMvvm<com.example.myandroidapp.databinding.ActivityInsertTodoBinding, InsertUpdateViewModel>(),
    InsertUpdateNavigator {


    var insertUpdateViewModel: InsertUpdateViewModel? = null
    override fun goBack() {
        finish()
    }

//    @set:Inject
//    var dataBaseHelper: RoomDataBaseHelper? = null

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.activity_insert_todo

    override fun getViewModel(): InsertUpdateViewModel? {
//        val factory = InsertUpdateViewModel.Factory(dataBaseHelper)
        insertUpdateViewModel = ViewModelProviders.of(this)
            .get(InsertUpdateViewModel::class.java)
        insertUpdateViewModel?.setNavigator(this)
        return insertUpdateViewModel

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


}
