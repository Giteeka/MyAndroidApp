package zebpay.Application.ui.base


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import zebpay.Application.constants.GlobalArea
import zebpay.Application.data.managers.AnalyticsManager
import zebpay.Application.data.models.ZebPayEvent
import zebpay.Application.data.models.country.CountryConfigResponse
import zebpay.Application.data.remote.apimanager.ApiResponse
import zebpay.Application.data.remote.apimanager.EnumManager
import zebpay.Application.data.remote.apimanager.repository.NetworkRepository
import zebpay.Application.data.remote.apimanager.responses.NewApiServerResponse
import zebpay.Application.data.remote.apimanager.responses.ResponseMultipleCurrencyBalance
import zebpay.Application.utils.CrashlyticsUtils
import zebpay.Application.utils.NetworkUtil
import java.lang.ref.WeakReference


abstract class BaseViewModel<N : BaseNavigator> : ViewModel() {

    private val mIsLoading = MutableLiveData<Boolean>()
    private var mNavigator: WeakReference<N>? = null

    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    companion object {
        private val TAG = BaseViewModel::class.java.simpleName
    }


    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
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


    fun getBalance(virtualCurrencyCode: String, show: Boolean) {
        if (!NetworkUtil.isInternetAvailable()) {
            getNavigator()?.noInternetMessage()
            return
        }

        if (show) setIsLoading(true)
        compositeDisposable.add(NetworkRepository.getInstance().getBalance(virtualCurrencyCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ResponseMultipleCurrencyBalance>() {
                    override fun onSuccess(resposne: ResponseMultipleCurrencyBalance) {
                        if (isValidResponse(resposne) && resposne.returncode == EnumManager.APIReturnCodes.Success.getValue()) {
                            if (resposne.balance != null && resposne.balance.size > 0) {
                                getNavigator()?.onSuccessOfGetBalance(resposne)
                            }
                        }
                        if (show) setIsLoading(false)
                    }

                    override fun onError(e: Throwable) {
                        if (show) setIsLoading(false)
                    }
                })
        )
    }


    fun isValidResponse(response: ApiResponse): Boolean {
        when {
            response.returncode == EnumManager.APIReturnCodes.InvalidAccount.getValue()
                    || response.returncode == EnumManager.APIReturnCodes.InvalidTransactionFeesInsatoshi.value
                    || response.returncode == EnumManager.APIReturnCodes.OtherException.value
                    || response.returncode == EnumManager.APIReturnCodes.ApiUrlNotfound.value
                    || response.returncode == EnumManager.APIReturnCodes.InvalidRequestId.value
                    || response.returncode == EnumManager.APIReturnCodes.ConnectionTimeout.value -> {
                getNavigator()?.onError(response.err)
            }

            response.returncode == EnumManager.APIReturnCodes.InvalidPhoneHash.value -> {
                getNavigator()?.onInvalidHashcodeError(response.err)
            }

            GlobalArea.validateReturnCodeForAuthentication(response.returncode) -> {
                getNavigator()?.onAuthenticationError()
            }

            response.returncode == EnumManager.APIReturnCodes.AccountBlocked.value -> {
                getNavigator()?.onAccountBlockedError(response.blockReason, response.blocked)
            }

            else -> {
                if (response.returncode != EnumManager.APIReturnCodes.Success.value) {
                    getNavigator()?.onError(response.err)
                }

                return true
            }
        }
        return false
    }

    fun setAnalyticScreenEvent(screenName: String) {
        AnalyticsManager.setScreen(screenName)
    }

    fun setAnalyticEvent(event: ZebPayEvent) {
        AnalyticsManager.setEvent(event)
    }

    /**
     * call country config api
     *
     * @param value   it is country config value received in firebase setting listener. passed it empty
     * if not call from setting listener.
     * @param country country three later  code : e.g IND
     *
     *
     * api will be triggered in following scenario:
     * 1) from splash screen on language/region changed
     * 2) Whenever fire-base  GUID changes
     * 3) On app force updates.
     */
    fun callCountyConfigAPI(value: String, country: String, isProgressDialog: Boolean) {

        if (isProgressDialog) getNavigator()?.showLoading()
        compositeDisposable.add(NetworkRepository.getInstance().countryConfiguration(country)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<NewApiServerResponse<CountryConfigResponse>>() {
                    override fun onSuccess(retunAccount: NewApiServerResponse<CountryConfigResponse>) {
                        if (isProgressDialog) getNavigator()?.hideLoading()
                        if (retunAccount.statusCode == EnumManager.APIReturnCodes.Success.getValue() && retunAccount.data != null) {

                            getNavigator()?.saveCountryConfigData(value, country, retunAccount.data)

                            if (retunAccount.data.countryDetail.isAllowsRegistration) {
                                getNavigator()?.onCountryConfigSuccess()
                            } else {
                                getNavigator()?.onCountryNoSupported()
                            }

                        } else if (retunAccount.statusCode == EnumManager.APIReturnCodes.InvalidCountry.getValue()) {
                            getNavigator()?.onCountryNoSupported()
                            getNavigator()?.setCountryConfigCalled(false)
                        } else {
                            getNavigator()?.setCountryConfigCalled(false)
                            getNavigator()?.onCountryConfigFailure(retunAccount.statusDescription)
                        }
                    }

                    override fun onError(ex: Throwable) {
                        if (isProgressDialog) getNavigator()?.hideLoading()
                        CrashlyticsUtils.log(TAG, "defaultTxSwitchState", ex)
                    }
                }))
    }


    fun getAccountDetailsApi() {

    }


}
