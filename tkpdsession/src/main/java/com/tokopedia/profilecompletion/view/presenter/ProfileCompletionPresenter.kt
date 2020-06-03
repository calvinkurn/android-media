package com.tokopedia.profilecompletion.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.core.util.SessionHandler
import com.tokopedia.profilecompletion.domain.EditUserProfileUseCase
import com.tokopedia.profilecompletion.domain.EditUserProfileUseCase.Companion.generateParamDOB
import com.tokopedia.profilecompletion.domain.EditUserProfileUseCase.Companion.generateParamGender
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase.Companion.generateParam
import com.tokopedia.profilecompletion.view.subscriber.EditUserInfoSubscriber
import com.tokopedia.profilecompletion.view.subscriber.GetUserInfoSubscriber
import com.tokopedia.session.R
import javax.inject.Inject

/**
 * Created by stevenfredian on 6/22/17.
 */
class ProfileCompletionPresenter @Inject internal constructor(private val getUserInfoUseCase: GetUserInfoUseCase,
                                                              private val editUserProfileUseCase: EditUserProfileUseCase,
                                                              private val sessionHandler: SessionHandler) : BaseDaggerPresenter<ProfileCompletionContract.View?>(), ProfileCompletionContract.Presenter {
    override fun attachView(view: ProfileCompletionContract.View?) {
        super.attachView(view)
        userInfo
    }

    override fun detachView() {
        super.detachView()
        getUserInfoUseCase.unsubscribe()
        editUserProfileUseCase.unsubscribe()
    }

    override val userInfo: Unit
        get() {
            getUserInfoUseCase.execute(generateParam(), GetUserInfoSubscriber(view!!))
        }

    override fun editUserInfo(date: String?, month: Int, year: String?) {
        if (date!!.isEmpty() || year!!.isEmpty() || month == 0) {
            view!!.onFailedEditProfile(view!!.getString(R.string.invalid_date))
        } else {
            view!!.disableView()
            editUserProfileUseCase.execute(generateParamDOB(date, month.toString(), year), EditUserInfoSubscriber(view!!, EditUserProfileUseCase.EDIT_DOB))
        }
    }

    override fun editUserInfo(gender: Int) {
        if (gender == -1) {
            view!!.onFailedEditProfile(view!!.getString(R.string.invalid_gender))
        } else {
            view!!.disableView()
            editUserProfileUseCase.execute(generateParamGender(gender), EditUserInfoSubscriber(view!!
                    , EditUserProfileUseCase.EDIT_GENDER))
        }
    }

    override fun editUserInfo(verif: String?) {}
    override fun skipView(tag: String?) {
        view!!.skipView(tag)
    }

    fun setMsisdnVerifiedToCache(isVerified: Boolean) {
        SessionHandler.setIsMSISDNVerified(isVerified)
    }

}