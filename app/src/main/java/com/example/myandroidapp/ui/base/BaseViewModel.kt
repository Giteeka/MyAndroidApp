package com.example.myandroidapp.ui.base


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myandroidapp.data.AppDbHelper
import com.example.myandroidapp.data.DpHelper
import com.example.myandroidapp.data.FirebaseDatabaseHelper
import com.example.myandroidapp.data.RoomDataBaseHelper
import java.lang.ref.WeakReference
import javax.inject.Inject


abstract class BaseViewModel<N : BaseNavigator> : ViewModel() {

    private val mIsLoading = MutableLiveData<Boolean>()
    private var mNavigator: WeakReference<N>? = null

    @set:Inject
    public var dpHelper: DpHelper? = null

    @set:Inject
    public var fbHelper: FirebaseDatabaseHelper? = null

    companion object {
        private val TAG = BaseViewModel::class.java.simpleName
    }

    init {
        fbHelper = FirebaseDatabaseHelper()
    }

    fun setIsLoading(isLoading: Boolean) {
        mIsLoading.value = isLoading
    }

    fun getNavigator(): N? {
        return mNavigator?.get()
    }

    fun setNavigator(navigator: N) {
        this.mNavigator = WeakReference(navigator)
    }


}
