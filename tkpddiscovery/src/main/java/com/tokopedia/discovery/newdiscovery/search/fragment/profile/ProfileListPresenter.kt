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
//        when(page) {
//            1 -> {
//                val prof1 = ProfileViewModel(
//                        id = "1",
//                        name = "Profile 1",
//                        imgUrl = "https://vignette.wikia.nocookie.net/abridgedseries/images/5/51/Naruto_Sagas_-_Sasuke_Uchiha_Character_Profile_Picture.jpg/revision/latest?cb=20170105154138",
//                        username = "@profile1",
//                        bio = "biology",
//                        followed = true,
//                        isKol = true,
//                        isAffiliate = true,
//                        following = 10,
//                        followers = 10,
//                        post_count = 15
//                )
//
//                val prof2 = ProfileViewModel(
//                        id = "1",
//                        name = "Profile 2",
//                        imgUrl = "https://vignette.wikia.nocookie.net/narutoprofile/images/3/36/Naruto_Uzumaki.png/revision/latest/scale-to-width-down/300?cb=20140222024015",
//                        username = "@profile1",
//                        bio = "biology",
//                        followed = false,
//                        isKol = true,
//                        isAffiliate = true,
//                        following = 10,
//                        followers = 10,
//                        post_count = 20
//                )
//                val prof3 = ProfileViewModel(
//                        id = "1",
//                        name = "Profile 1",
//                        imgUrl = "https://vignette.wikia.nocookie.net/abridgedseries/images/5/51/Naruto_Sagas_-_Sasuke_Uchiha_Character_Profile_Picture.jpg/revision/latest?cb=20170105154138",
//                        username = "@profile1",
//                        bio = "biology",
//                        followed = true,
//                        isKol = true,
//                        isAffiliate = true,
//                        following = 10,
//                        followers = 10,
//                        post_count = 15
//                )
//
//                val prof4 = ProfileViewModel(
//                        id = "1",
//                        name = "Profile 2",
//                        imgUrl = "https://vignette.wikia.nocookie.net/narutoprofile/images/3/36/Naruto_Uzumaki.png/revision/latest/scale-to-width-down/300?cb=20140222024015",
//                        username = "@profile1",
//                        bio = "biology",
//                        followed = false,
//                        isKol = true,
//                        isAffiliate = true,
//                        following = 10,
//                        followers = 10,
//                        post_count = 20
//                )
//                val prof5 = ProfileViewModel(
//                        id = "1",
//                        name = "Profile 1",
//                        imgUrl = "https://vignette.wikia.nocookie.net/abridgedseries/images/5/51/Naruto_Sagas_-_Sasuke_Uchiha_Character_Profile_Picture.jpg/revision/latest?cb=20170105154138",
//                        username = "@profile1",
//                        bio = "biology",
//                        followed = true,
//                        isKol = true,
//                        isAffiliate = true,
//                        following = 10,
//                        followers = 10,
//                        post_count = 15
//                )
//
//                val prof6 = ProfileViewModel(
//                        id = "1",
//                        name = "Profile 2",
//                        imgUrl = "https://vignette.wikia.nocookie.net/narutoprofile/images/3/36/Naruto_Uzumaki.png/revision/latest/scale-to-width-down/300?cb=20140222024015",
//                        username = "@profile1",
//                        bio = "biology",
//                        followed = false,
//                        isKol = true,
//                        isAffiliate = true,
//                        following = 10,
//                        followers = 10,
//                        post_count = 20
//                )
//                val datas = ArrayList<ProfileViewModel>()
//                datas.add(prof1)
//                datas.add(prof2)
//                datas.add(prof3)
//                datas.add(prof4)
//                datas.add(prof5)
//                datas.add(prof6)
//                val profileListViewModel = ProfileListViewModel(
//                        datas,
//                        true
//                )
//                view.onSuccessGetProfileListData(profileListViewModel)
//            }
//            else -> {
//                val lm1 = ProfileViewModel(
//                        id = "1",
//                        name = "LoadMore 1",
//                        imgUrl = "https://img-cache.cdn.gaiaonline.com/765cac67c08df7fcb118d4d6b98fc145/http://i222.photobucket.com/albums/dd312/ShikaIno_12/Tsunade_Obaa_chan_by_saishuu_hinoir.jpg",
//                        username = "@profile1",
//                        bio = "biology",
//                        followed = true,
//                        isKol = true,
//                        isAffiliate = true,
//                        following = 10,
//                        followers = 10,
//                        post_count = 15
//                )
//
//                val lm2 = ProfileViewModel(
//                        id = "1",
//                        name = "LoadMore 2",
//                        imgUrl = "https://vignette.wikia.nocookie.net/naruto-bleach/images/e/ea/Tsunade2.jpg/revision/latest/scale-to-width-down/260?cb=20120501131716",
//                        username = "@profile1",
//                        bio = "biology",
//                        followed = false,
//                        isKol = true,
//                        isAffiliate = true,
//                        following = 10,
//                        followers = 10,
//                        post_count = 20
//                )
//                val datas = ArrayList<ProfileViewModel>()
//                datas.add(lm1)
//                datas.add(lm2)
//                val profileListViewModel = ProfileListViewModel(
//                        datas,
//                        false
//                )
//                view.onSuccessGetProfileListData(profileListViewModel)
//            }
//        }
    }

}