package com.tokopedia.profilecompletion.data.repository

import com.tokopedia.core.profile.model.GetUserInfoDomainModel
import com.tokopedia.profilecompletion.data.factory.ProfileSourceFactory
import rx.Observable

/**
 * @author by nisie on 6/19/17.
 */
class ProfileRepositoryImpl(private val profileSourceFactory: ProfileSourceFactory) : ProfileRepository {
    override fun getUserInfo(parameters: Map<String?, Any?>?): Observable<GetUserInfoDomainModel> {
        return profileSourceFactory.createCloudGetUserInfoSource().getUserInfo(parameters)
    }
}