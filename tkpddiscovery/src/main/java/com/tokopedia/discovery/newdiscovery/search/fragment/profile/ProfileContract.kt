package com.tokopedia.discovery.newdiscovery.search.fragment.profile

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.discovery.newdiscovery.base.RedirectionListener
import com.tokopedia.discovery.newdiscovery.search.SearchNavigationListener
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.listener.FollowActionListener
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.ProfileListViewModel
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.ProfileViewModel
import java.util.ArrayList

class ProfileContract {
    interface View : CustomerView{
        fun onSuccessGetProfileListData(profileListViewModel : ProfileListViewModel)
        fun attachNavigationListener(searchNavigationListener: SearchNavigationListener)
        fun attachRedirectionListener(redirectionListener: RedirectionListener)
        fun onErrorGetProfileListData(e : Throwable)
        fun launchLoginPage()
        fun launchProfilePage(userId : String)
    }

    interface Presenter : CustomerPresenter<View> {
        fun attachFollowActionListener(followActionListener: FollowActionListener)
        fun requestProfileListData(query: String, page: Int)
        fun handleFollowAction(adapterPosition: Int,
                               userToFollowId: Int,
                               followedStatus: Boolean)
    }
}