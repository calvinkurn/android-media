package com.tokopedia.profilecompletion.data.source

import android.content.Context
import com.tokopedia.core.profile.model.GetUserInfoDomainModel
import com.tokopedia.core.util.SessionHandler
import com.tokopedia.network.service.AccountsService
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable
import rx.functions.Action1

/**
 * @author by nisie on 6/19/17.
 */
class CloudGetUserInfoSource(private val context: Context,
                             private val accountsService: AccountsService,
                             private val getUserInfoMapper: GetUserInfoMapper,
                             private val sessionHandler: SessionHandler) {
    fun getUserInfo(parameters: Map<String?, Any?>?): Observable<GetUserInfoDomainModel> {
        return accountsService.api
                .getUserInfo(parameters)
                .map(getUserInfoMapper)
                .doOnNext(saveToCache())
    }

    private fun saveToCache(): Action1<GetUserInfoDomainModel> {
        return Action1 { getUserInfoDomainModel ->
            val userSession: UserSessionInterface = UserSession(context)
            if (!userSession.isLoggedIn) {
                sessionHandler.setTempLoginSession(getUserInfoDomainModel
                        .getUserInfoDomainData.userId.toString())
                sessionHandler.setTempPhoneNumber(getUserInfoDomainModel.getUserInfoDomainData.phone)
                sessionHandler.setTempLoginName(getUserInfoDomainModel
                        .getUserInfoDomainData.fullName)
                sessionHandler.setTempLoginEmail(getUserInfoDomainModel
                        .getUserInfoDomainData.email)
            }
            sessionHandler.setHasPassword(getUserInfoDomainModel.getUserInfoDomainData
                    .isCreatedPassword)
            sessionHandler.setProfilePicture(getUserInfoDomainModel
                    .getUserInfoDomainData.profilePicture)
        }
    }
}