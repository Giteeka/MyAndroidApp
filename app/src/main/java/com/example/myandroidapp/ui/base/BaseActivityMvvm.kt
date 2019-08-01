package com.example.myandroidapp.ui.base

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.myandroidapp.R

abstract class BaseActivityMvvm<T : ViewDataBinding, V : BaseViewModel<*>> : AppCompatActivity(),
    BaseNavigator {

    private var mActivity: Activity? = null
    private var mProgressDialog: ProgressDialog? = null
    private var showOnlineStatusView = true
    var viewDataBinding: T? = null
    private var mViewModel: V? = null

    companion object {
        private val TAG = BaseActivityMvvm::class.java.simpleName
    }

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract fun getViewModel(): V?

    fun setShowOnlineStatusView(showOnlineStatusView: Boolean) {
        this.showOnlineStatusView = showOnlineStatusView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        adjustFontScale(resources.configuration)
        mActivity = this
        //        baseViewModel = ViewModelProviders.of(this).get(BaseViewModel.class);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)


    }

    private fun adjustFontScale(configuration: Configuration) {
        configuration.fontScale = 1.0.toFloat()
        val metrics = resources.displayMetrics
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        baseContext.resources.updateConfiguration(configuration, metrics)
    }

    override fun showLoading() {
        hideProgress()
        showProgress()
    }

    override fun hideLoading() {
        hideProgress()
    }

    override fun showMessage(message: String) {
//        Utils.showDefaultSnackbarWithAction(this, message)
    }


    private fun showProgress() {
        if (!isFinishing) {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog?.setMessage(getString(R.string.please_wait))
            mProgressDialog?.isIndeterminate = false
            mProgressDialog?.setCancelable(false)
            mProgressDialog?.show()
        }
    }

    private fun hideProgress() {
        if (mProgressDialog?.isShowing == true && !isFinishing) {
            mProgressDialog?.cancel()
        }
    }

    override fun isProgressVisible(): Boolean {
        return mProgressDialog != null && mProgressDialog?.isShowing ?: false
    }

    /**
     * @param toolbar
     * @param title
     * @param isBackEnabled
     */
    protected fun setToolbar(toolbar: Toolbar?, title: String, isBackEnabled: Boolean) {
        super.setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar?.title = title
        if (isBackEnabled) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)
            toolbar?.setNavigationOnClickListener { onBackPressed() }
        }
    }

    /**
     * @param toolbar
     */
    protected fun setToolbar(toolbar: Toolbar) {
        super.setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }


    private fun showOnlineOffline() {
        if (!showOnlineStatusView)
            return
//        try {
//            val isonline = Utils.isOnline()


    }


    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        this.mViewModel = if (mViewModel == null) getViewModel() else mViewModel
        viewDataBinding?.setVariable(bindingVariable, mViewModel)
        viewDataBinding?.executePendingBindings()
    }


    override fun noInternetMessage() {
//        Utils.showDefaultSnackbarWithAction(this, Constants.GlobalToast.NO_NETWORK_CONNECTION)
    }

    override fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}

