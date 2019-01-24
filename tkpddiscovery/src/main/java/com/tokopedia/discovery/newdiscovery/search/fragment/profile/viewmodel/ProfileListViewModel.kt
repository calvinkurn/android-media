package com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel

class ProfileListViewModel(val profileModelList: List<ProfileViewModel>,
                           val isHasNextPage: Boolean,
                           val totalSearchCount: Int) {

    fun getListTrackingObject() : MutableList<Any> {
        val listTracking = mutableListOf(Any())
        for(profileModel in profileModelList) {
            listTracking.add(profileModel.getTrackingObject())
        }
        return listTracking
    }
}
