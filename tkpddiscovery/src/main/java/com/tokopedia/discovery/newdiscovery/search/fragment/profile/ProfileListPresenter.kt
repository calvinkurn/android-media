package com.tokopedia.discovery.newdiscovery.search.fragment.profile

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.discovery.newdiscovery.domain.subscriber.GetProfileListSubscriber
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProfileListUseCase
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.listener.FollowActionListener
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.subscriber.FollowUnfollowKolSubscriber
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.ProfileViewModel
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase

class ProfileListPresenter(
        val getProfileListUseCase: GetProfileListUseCase,
        val followKolPostGqlUseCase : FollowKolPostGqlUseCase)
    : BaseDaggerPresenter<ProfileContract.View>() , ProfileContract.Presenter{
    lateinit var followActionListener: FollowActionListener

    override fun attachView(view: ProfileContract.View?) {
        super.attachView(view)
    }

    override fun attachFollowActionListener(followActionListener: FollowActionListener) {
        this.followActionListener = followActionListener
    }

    override fun handleFollowAction(adapterPosition: Int,
                                    profileModel: ProfileViewModel) {
        val requestedAction : Int =
                when(profileModel.followed) {
                    true -> FollowKolPostGqlUseCase.PARAM_UNFOLLOW
                    false -> FollowKolPostGqlUseCase.PARAM_FOLLOW
                }

        followKolPostGqlUseCase.execute(
                FollowKolPostGqlUseCase.createRequestParams(
                        profileModel.id.toInt(),
                        requestedAction
                ), FollowUnfollowKolSubscriber(
                adapterPosition,
                view,
                profileModel.followed,
                followActionListener
        ))
    }

    override fun requestProfileListData(query: String, page: Int) {
        getProfileListUseCase.execute(
                GetProfileListUseCase.createRequestParams(
                        query,
                        page
                        ), GetProfileListSubscriber(view))
    }

}