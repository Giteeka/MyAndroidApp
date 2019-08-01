package com.example.myandroidapp.ui.base

interface BaseNavigator {

    fun noInternetMessage()
    fun showLoading()
    fun hideLoading()
    fun showMessage(message: String)
    fun showToastMessage(message: String)
    fun isProgressVisible(): Boolean
}