package com.tokopedia.discovery.newdiscovery.search.fragment.profile.subscriber

import com.tokopedia.discovery.newdiscovery.search.fragment.profile.ProfileContract
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.listener.FollowActionListener
import com.tokopedia.kolcommon.model.FollowResponseModel

import rx.Subscriber

class FollowUnfollowKolSubscriber(val adapterPosition: Int,
                                  val view: ProfileContract.View,
                                  val followStatus: Boolean,
                                  val followActionListener: FollowActionListener) : Subscriber<FollowResponseModel>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        followActionListener.onErrorToggleFollow(adapterPosition, e.message!!)
    }

    override fun onNext(followResponseModel: FollowResponseModel) {
        if (followResponseModel.isSuccess) {
            followActionListener.onSuccessToggleFollow(adapterPosition, (!followStatus)!!)
        } else {
            followActionListener.onErrorToggleFollow(adapterPosition, followResponseModel.errorMessage)
        }
    }
}
