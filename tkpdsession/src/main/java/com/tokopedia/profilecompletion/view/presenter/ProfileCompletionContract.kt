package com.tokopedia.profilecompletion.view.presenter

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.profilecompletion.view.viewmodel.ProfileCompletionViewModel
import com.tokopedia.user.session.UserSession

/**
 * Created by stevenfredian on 6/22/17.
 */
interface ProfileCompletionContract : CustomerView {
    interface View : CustomerView {
        fun skipView(tag: String?)
        fun onGetUserInfo(profileCompletionViewModel: ProfileCompletionViewModel?)
        fun onErrorGetUserInfo(string: String?)
        fun onSuccessEditProfile(edit: Int)
        fun onFailedEditProfile(errorMessage: String?)
        fun getString(id: Int): String?
        fun disableView()
        val data: ProfileCompletionViewModel?
        fun canProceed(canProceed: Boolean)
        fun getPresenter(): Presenter?
        fun getView(): android.view.View?
        val userSession: UserSession?
    }

    interface Presenter : CustomerPresenter<View?> {
        val userInfo: Unit
        fun editUserInfo(date: String?, month: Int, year: String?)
        fun editUserInfo(gender: Int)
        fun editUserInfo(verif: String?)
        fun skipView(tag: String?)
    }
}