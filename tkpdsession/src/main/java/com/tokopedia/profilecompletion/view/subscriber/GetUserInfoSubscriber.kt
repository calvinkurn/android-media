package com.tokopedia.profilecompletion.view.subscriber

import com.tokopedia.core.network.retrofit.response.ErrorListener
import com.tokopedia.core.profile.model.GetUserInfoDomainData
import com.tokopedia.core.profile.model.GetUserInfoDomainModel
import com.tokopedia.network.ErrorHandler
import com.tokopedia.network.ErrorMessageException
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionContract
import com.tokopedia.profilecompletion.view.viewmodel.ProfileCompletionViewModel
import com.tokopedia.session.R
import rx.Subscriber
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author by nisie on 6/19/17.
 */
class GetUserInfoSubscriber(private val listener: ProfileCompletionContract.View) : Subscriber<GetUserInfoDomainModel?>() {
    override fun onCompleted() {}
    override fun onError(e: Throwable) {
        Timber.d("NISNIS error$e")
        if (e is UnknownHostException) {
            listener.onErrorGetUserInfo(listener.getString(R.string.msg_no_connection))
        } else if (e is SocketTimeoutException) {
            listener.onErrorGetUserInfo(listener.getString(R.string.default_request_error_timeout))
        } else if (e is IOException) {
            listener.onErrorGetUserInfo(listener.getString(R.string.default_request_error_internal_server))
        } else if (e is RuntimeException && e.getLocalizedMessage() != null && e.getLocalizedMessage().length <= 3) {
            ErrorHandler(object : ErrorListener {
                override fun onUnknown() {
                    listener.onErrorGetUserInfo(
                            listener.getString(R.string.default_request_error_unknown))
                }

                override fun onTimeout() {
                    listener.onErrorGetUserInfo(
                            listener.getString(R.string.default_request_error_timeout))
                }

                override fun onServerError() {
                    listener.onErrorGetUserInfo(
                            listener.getString(R.string.default_request_error_internal_server))
                }

                override fun onBadRequest() {
                    listener.onErrorGetUserInfo(
                            listener.getString(R.string.default_request_error_bad_request))
                }

                override fun onForbidden() {
                    listener.onErrorGetUserInfo(
                            listener.getString(R.string.default_request_error_forbidden_auth))
                }
            }, e.getLocalizedMessage().toInt())
        } else if (e is ErrorMessageException
                && e.getLocalizedMessage() != null) {
            listener.onErrorGetUserInfo(e.getLocalizedMessage())
        } else {
            listener.onErrorGetUserInfo(listener.getString(R.string.default_request_error_unknown))
        }
    }

    override fun onNext(getUserInfoDomainModel: GetUserInfoDomainModel?) {
        Timber.d("NISNIS sukses")
        listener.onGetUserInfo(getUserInfoDomainModel?.getUserInfoDomainData?.let { mappingToViewModel(it) })
    }

    private fun mappingToViewModel(getUserInfoDomainData: GetUserInfoDomainData): ProfileCompletionViewModel {
        return ProfileCompletionViewModel(
                getUserInfoDomainData.gender,
                getUserInfoDomainData.phone,
                getUserInfoDomainData.bday,
                getUserInfoDomainData.completion,
                getUserInfoDomainData.isPhoneVerified)
    }

}