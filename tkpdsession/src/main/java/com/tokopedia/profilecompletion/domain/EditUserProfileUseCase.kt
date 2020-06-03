package com.tokopedia.profilecompletion.domain

import com.tokopedia.core.base.domain.RequestParams
import com.tokopedia.core.base.domain.UseCase
import com.tokopedia.core.base.domain.executor.PostExecutionThread
import com.tokopedia.core.base.domain.executor.ThreadExecutor
import com.tokopedia.profilecompletion.data.repository.ProfileRepository
import com.tokopedia.profilecompletion.domain.model.EditUserInfoDomainModel
import rx.Observable

/**
 * @author by nisie on 7/3/17.
 */
class EditUserProfileUseCase(threadExecutor: ThreadExecutor?, postExecutionThread: PostExecutionThread?,
                             private val profileRepository: ProfileRepository) : UseCase<EditUserInfoDomainModel>(threadExecutor, postExecutionThread) {

    override fun createObservable(requestParams: RequestParams): Observable<EditUserInfoDomainModel> {
        return profileRepository.editUserInfo(requestParams.parameters)
    }

    companion object {
        const val EDIT_GENDER = 1
        const val EDIT_DOB = 2
        const val EDIT_VERIF = 3
        private const val BDAY_DATE = "bday_dd"
        private const val BDAY_MONTH = "bday_mm"
        private const val BDAY_YEAR = "bday_yy"
        private const val GENDER = "gender"
        const val MALE = 1
        const val FEMALE = 2
        @JvmStatic
        fun generateParamDOB(day: String?, month: String?, year: String?): RequestParams {
            val params = RequestParams.create()
            params.putString(BDAY_DATE, day)
            params.putString(BDAY_MONTH, month)
            params.putString(BDAY_YEAR, year)
            return params
        }

        @JvmStatic
        fun generateParamGender(gender: Int): RequestParams {
            val params = RequestParams.create()
            params.putInt(GENDER, gender)
            return params
        }
    }

}