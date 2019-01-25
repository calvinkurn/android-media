package com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel

open class ProfileListViewModel(val profileModelList: List<ProfileViewModel>,
                           val isHasNextPage: Boolean,
                           val totalSearchCount: Int) {

    fun getListTrackingObject() : List<Any> {
        val listTracking = arrayListOf<Any>()
        for(profileModel in profileModelList) {
            listTracking.add(profileModel.getTrackingObject())
        }
        return listTracking
    }
}
