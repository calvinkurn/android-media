package com.tokopedia.discovery.newdiscovery.domain.subscriber

import com.tokopedia.discovery.newdiscovery.search.fragment.profile.ProfileContract
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.ProfileListViewModel
import rx.Subscriber

open class GetProfileListSubscriber(val profileView: ProfileContract.View) : Subscriber<ProfileListViewModel>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
//        profileView.handleErrorResult(e)
    }

    override fun onNext(profileListViewModel: ProfileListViewModel) {
        profileView.onSuccessGetProfileListData(
                profileListViewModel
        )
    }
}
