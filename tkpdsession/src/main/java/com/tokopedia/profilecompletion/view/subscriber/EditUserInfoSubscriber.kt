package com.tokopedia.profilecompletion.view.subscriber

import com.tokopedia.core.network.retrofit.response.ErrorListener
import com.tokopedia.network.ErrorHandler
import com.tokopedia.network.ErrorMessageException
import com.tokopedia.profilecompletion.domain.model.EditUserInfoDomainModel
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionContract
import com.tokopedia.session.R
import rx.Subscriber
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author by nisie on 7/3/17.
 */
class EditUserInfoSubscriber(private val listener: ProfileCompletionContract.View, private val edit: Int) : Subscriber<EditUserInfoDomainModel?>() {
    override fun onCompleted() {}
    override fun onError(e: Throwable) {
        Timber.d("NISNIS error$e")
        if (e is UnknownHostException) {
            listener.onFailedEditProfile(listener.getString(R.string.msg_no_connection))
        } else if (e is SocketTimeoutException) {
            listener.onFailedEditProfile(listener.getString(R.string.default_request_error_timeout))
        } else if (e is IOException) {
            listener.onFailedEditProfile(listener.getString(R.string.default_request_error_internal_server))
        } else if (e is RuntimeException && e.getLocalizedMessage() != null && e.getLocalizedMessage().length <= 3) {
            ErrorHandler(object : ErrorListener {
                override fun onUnknown() {
                    listener.onFailedEditProfile(
                            listener.getString(R.string.default_request_error_unknown))
                }

                override fun onTimeout() {
                    listener.onFailedEditProfile(
                            listener.getString(R.string.default_request_error_timeout))
                }

                override fun onServerError() {
                    listener.onFailedEditProfile(
                            listener.getString(R.string.default_request_error_internal_server))
                }

                override fun onBadRequest() {
                    listener.onFailedEditProfile(
                            listener.getString(R.string.default_request_error_bad_request))
                }

                override fun onForbidden() {
                    listener.onFailedEditProfile(
                            listener.getString(R.string.default_request_error_forbidden_auth))
                }
            }, e.getLocalizedMessage().toInt())
        } else if (e is ErrorMessageException
                && e.getLocalizedMessage() != null) {
            listener.onFailedEditProfile(e.getLocalizedMessage())
        } else {
            listener.onFailedEditProfile(listener.getString(R.string.default_request_error_unknown))
        }
        Timber.d("NISNIS error$e")
    }

    override fun onNext(editUserInfoDomainModel: EditUserInfoDomainModel?) {
        Timber.d("NISNIS sukses")
        if (editUserInfoDomainModel?.isSuccess == true) {
            listener.onSuccessEditProfile(edit)
        } else {
            listener.onFailedEditProfile(editUserInfoDomainModel?.errorMessage)
        }
    }

}