package com.tokopedia.profilecompletion.data.repository

import com.tokopedia.core.profile.model.GetUserInfoDomainModel
import rx.Observable

/**
 * @author by nisie on 6/19/17.
 */
interface ProfileRepository {
    fun getUserInfo(parameters: Map<String?, Any?>?): Observable<GetUserInfoDomainModel>
}