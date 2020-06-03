package com.tokopedia.profilecompletion.domain

import com.tokopedia.core.profile.model.GetUserInfoDomainModel
import com.tokopedia.profilecompletion.data.repository.ProfileRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * @author by nisie on 6/19/17.
 */
class GetUserInfoUseCase(private val profileRepository: ProfileRepository) : UseCase<GetUserInfoDomainModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<GetUserInfoDomainModel> {
        return profileRepository.getUserInfo(requestParams.parameters)
    }

    companion object {
        @JvmStatic
        fun generateParam(): RequestParams {
            return RequestParams.EMPTY
        }
    }

}