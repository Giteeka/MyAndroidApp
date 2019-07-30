package zebpay.Application.ui.base

import android.app.Activity
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivityMvvm<T : ViewDataBinding, V : BaseViewModel<*>> : AppCompatActivity(),
     BaseNavigator{
    private lateinit var mCompositeDisposable: CompositeDisposable
    internal var playstoreReferrelBroadcast: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

        }
    }
    private var mActivity: Activity? = null
    private var mProgressDialog: ProgressDialog? = null
    private var receiver: BroadcastReceiver? = null
    private var intentFilter: IntentFilter? = null
    private var mAppMsg: AppMsg? = null
    private var mLoginDialog: LoginDialog? = null
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
        try {
            if (SharedPref.getBooleanPreferences(Constants.PREF_UPDATE_APP) && SharedPref
                    .getBooleanPreferences(Constants.PREF_APP_IS_FORCE_UPDATE)
            ) {
                updateAppDialog()
            }
        } catch (ex: Exception) {
            CrashlyticsUtils.log(TAG, "onCreate")
        }

        intentFilter = IntentFilter()
        intentFilter?.addAction(CONNECTIVITY_ACTION)
        receiver = NetworkChangeReceiver(this)

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
        Utils.showDefaultSnackbarWithAction(this, message)
    }

    override fun onResume() {
        try {
            super.onResume()
            registerReceiver(receiver, intentFilter)
            registerReceiver(playstoreReferrelBroadcast, IntentFilter("referral"))
            SettingListenerManager.getInstance().setSettingListener(this)
            if (Utils.isSessionActive()) {
                WarningMessageListenerHelper.getInstance()
            }
            Utils.setActivityResumeLockTime()
            val shouldShowPin = Utils.shouldShowPin()
            val isAccountBlock = Utils.isAccountBlock()
            if (shouldShowPin && !isAccountBlock) {
                startActivity(Intent(Zebpay.getInstance(), PinLockActivity::class.java))
                finishAffinity()
            }
        } catch (ex: Exception) {
            CrashlyticsUtils.log(TAG, "onResume", ex)
        }

    }

    override fun onPause() {
        super.onPause()
        try {
            if (receiver != null) {
                unregisterReceiver(receiver)
            }

            if (playstoreReferrelBroadcast != null) {
                unregisterReceiver(playstoreReferrelBroadcast)
            }
            Utils.setActivityPauseLockTime()
        } catch (ex: Exception) {
            CrashlyticsUtils.log(TAG, "onPause", ex)
        }

    }

    private fun showProgress() {
        Zebpay.getIdlingResource().setIdleState(false)
        if (!isFinishing) {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog?.setMessage(getString(R.string.please_wait))
            mProgressDialog?.isIndeterminate = false
            mProgressDialog?.setCancelable(false)
            mProgressDialog?.show()
        }
    }

    private fun hideProgress() {
        Zebpay.getIdlingResource().setIdleState(true)
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

    override fun openSubmissionScreen() {
        GlobalScreens.openSubmissionScreen(mActivity)
    }

    override fun openSplashScreen() {
        GlobalScreens.openSplashScreenAndClearAllData(mActivity)
    }

    override fun openVerifyingMobilenoScreen() {
        GlobalScreens.openSplashScreenAndClearAllData(mActivity)
    }

    override fun openAccountBlockScreen(blockReason: String) {
        GlobalScreens.openAccountBlockScreen(mActivity, blockReason)
    }

    override fun showLoginAlert(message: String) {
        if (!Utils.isSessionActive()) {
            if (mLoginDialog == null) {
                mLoginDialog = mActivity?.let { LoginDialog(it) }
                mLoginDialog?.show()
            } else {
                if (mLoginDialog?.isShowing == false) {
                    mLoginDialog?.show()
                }
            }
        }
    }

    private fun updateAppDialog() {
        try {
            val updateAppPopup = UpdateAppDialog(this)
            updateAppPopup.show()
            if (!GlobalArea.isEmpty(SharedPref.getPreferences(Constants.PREF_APP_FORCE_UPDATE_TEXT))) {
                updateAppPopup.setUpdateMessage(SharedPref.getPreferences(Constants.PREF_APP_FORCE_UPDATE_TEXT))
            }
            updateAppPopup.setCancelable(false)

            updateAppPopup.setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finish()
                }
                true
            }
        } catch (ex: Exception) {
            CrashlyticsUtils.log(TAG, "updateAppDialog")
        }

    }

    private fun showOnlineOffline() {
        if (!showOnlineStatusView)
            return
        try {
            val isonline = Utils.isOnline()
            if (mAppMsg != null && mAppMsg?.isShowing == true) {
                mAppMsg?.cancel()
            }
            if (isonline) {
                if (SharedPref.getPreferences(Constants.PREF_LASTSTATUS, "") == "off") {
                    val style = AppMsg.Style(AppMsg.LENGTH_SHORT, R.color.transparent)
                    mAppMsg = AppMsg.makeText(
                        this, getString(R.string.online), style, R
                            .layout.status_online_toast
                    )
                    mAppMsg?.setAnimation(
                        android.R.anim.slide_in_left, android.R.anim
                            .slide_out_right
                    )
                    mAppMsg?.show()
                }
                SharedPref.setPreferences(Constants.PREF_LASTSTATUS, "on")
            } else {
                val style = AppMsg.Style(AppMsg.LENGTH_STICKY, R.color.transparent)
                mAppMsg = AppMsg.makeText(
                    this, getString(R.string.offline), style, R.layout
                        .status_offline_toast
                )
                mAppMsg?.setAnimation(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                if (mAppMsg?.isShowing == false) {
                    mAppMsg?.show()
                }
                SharedPref.setPreferences(Constants.PREF_LASTSTATUS, "off")
            }
        } catch (ex: Exception) {
            CrashlyticsUtils.log(TAG, "showOnlineOffline", ex)
        }

    }

    override fun onCountryConfigGUIDChange(value: String, country: String) {
        getViewModel()?.callCountyConfigAPI(value, country, false)
    }

    override fun saveCountryConfigData(value: String, country: String, returnAccount: CountryConfigResponse) {
        SharedPref.setPreferences(Constants.PREF_COUNTRY_CONFIG_NEW, value)
        CountryManager.getInstance().setCountryConfiguration(returnAccount)
        SharedPref.setPreferences(Constants.PREF_COUNTRY_UNICODE, CountryManager.getInstance().countryUnicode)
        SharedPref.setPreferences(Constants.PREF_CURRENCY, CountryManager.getInstance().country.currency)
        SharedPref.setPreferences(
            Constants.PREF_COUNTRY_THREE_LATER_CODE,
            CountryManager.getInstance().country.countryThreeLetterIsoCode
        )
        SharedPref.setPreferences(Constants.PREF_DEFAULT_COUNTRY, returnAccount.countryDetail.defaultCountryCode)

        setCountryConfigCalled(true)
    }


    override fun onLocationSuccess(countryThreeLetterCode: String) {
        /*if (mPresenter != null) {
            mPresenter.onDetach();
        }*/
    }

    override fun onLocationFailure() {
        /*if (mPresenter != null) {
            mPresenter.onDetach();
        }*/
    }

    override fun onCountryConfigFailure(message: String) {
        Utils.showDefaultSnackbarWithAction(this, message)
    }

    override fun onCountryNoSupported() {

        /*if (!Utils.isSessionActive()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Not supported");
            builder.setMessage("Zebpay is not supported in " + CountryManager.getInstance().getCountry()
                    .getCountryDetail().getName());
            builder.setCancelable(true);
            builder.show();
        }*/
    }

    override fun onChange() {
        if (!showOnlineStatusView)
            return
        showOnlineOffline()
    }

    override fun onLoginClick() {
        GlobalScreens.openVerifyMobileScreen(this)
    }


    override fun onStart() {
        super.onStart()
        mCompositeDisposable = CompositeDisposable()
    }

    override fun onStop() {
        super.onStop()
        mCompositeDisposable.dispose()
        mCompositeDisposable.clear()
    }


    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        this.mViewModel = if (mViewModel == null) getViewModel() else mViewModel
        viewDataBinding?.setVariable(bindingVariable, mViewModel)
        viewDataBinding?.executePendingBindings()
    }

    override fun onAccountBlockedError(blockReason: String, isBlocked: Boolean) {
        GlobalScreens.openAccountBlockScreen(this, blockReason)
        SharedPref.setBooleanPreferences(Constants.PREF_ISACCOUNTBLOCK, isBlocked)
    }


    override fun onAuthenticationError() {
        GlobalArea.clearAuthentication()
        GlobalScreens.openSplashScreenAndClearAllData(this)
    }

    override fun onCountryConfigSuccess() {

    }

    override fun onError(s: String) {
        Utils.showDefaultSnackbarWithAction(this, s)
    }

    override fun showValidationMessage(s: String) {
        Utils.showDefaultSnackbar(this, s, false)
    }

    override fun onInvalidHashcodeError(s: String) {
        GlobalArea.clearPhoneHash()
        GlobalScreens.openSplashScreenAndClearAllData(this)
    }

    override fun setCountryConfigCalled(value: Boolean) {
        SharedPref.setBooleanPreferences(Constants.PREF_IS_COUNTRY_CONFIG_CALLED, value)
    }


    override fun onSuccessOfGetBalance(response: ResponseMultipleCurrencyBalance) {
        BalanceManager.getInstance().saveCurrencyBalance(response.balance)
        val messageEvent = MessageEvent(EnumManager.EventBusTypes.MultipleCurrencyBalanceResponse.value, response)
        EventBus.getDefault().post(messageEvent)
    }


    override fun noInternetMessage() {
        Utils.showDefaultSnackbarWithAction(this, Constants.GlobalToast.NO_NETWORK_CONNECTION)
    }

    override fun showToastMessage(message: String) {
        Utils.showDefaultToast(this, message, false)
    }
}

