package com.tokopedia.profilecompletion.data.repository

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam
import com.tokopedia.core.profile.model.GetUserInfoDomainModel
import com.tokopedia.profilecompletion.data.factory.ProfileSourceFactory
import com.tokopedia.profilecompletion.domain.model.EditUserInfoDomainModel
import rx.Observable

/**
 * @author by nisie on 6/19/17.
 */
class ProfileRepositoryImpl(private val profileSourceFactory: ProfileSourceFactory) : ProfileRepository {
    override fun getUserInfo(parameters: Map<String?, Any?>?): Observable<GetUserInfoDomainModel> {
        return profileSourceFactory.createCloudGetUserInfoSource().getUserInfo(parameters)
    }

    override fun editUserInfo(parameters: TKPDMapParam<String?, Any?>?): Observable<EditUserInfoDomainModel> {
        return profileSourceFactory.createCloudEditUserInfoSource().editUserInfo(parameters)
    }
}