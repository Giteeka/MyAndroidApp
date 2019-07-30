package zebpay.Application.ui.base

import zebpay.Application.data.models.country.CountryConfigResponse
import zebpay.Application.data.remote.apimanager.responses.ResponseMultipleCurrencyBalance

interface BaseNavigator {

    fun onAuthenticationError()
    fun showValidationMessage(s: String)
    fun onInvalidHashcodeError(s: String)
    fun onError(s: String)
    fun onAccountBlockedError(blockReason: String, isBlocked: Boolean)
    fun onSuccessOfGetBalance(response: ResponseMultipleCurrencyBalance)
    fun setCountryConfigCalled(value: Boolean)
    fun noInternetMessage()
    fun showLoading()
    fun hideLoading()
    fun showMessage(message: String)
    fun showToastMessage(message: String)
    fun isProgressVisible(): Boolean
    fun openSubmissionScreen()
    fun openSplashScreen()
    fun openVerifyingMobilenoScreen()
    fun openAccountBlockScreen(blockReason: String)
    fun showLoginAlert(message: String)
    fun onCountryConfigSuccess()
    fun saveCountryConfigData(value: String, country: String, returnAccount: CountryConfigResponse)
    fun onLocationSuccess(countryThreeLetterCode: String)
    fun onLocationFailure()
    fun onCountryNoSupported()
    fun onCountryConfigFailure(message: String)
}