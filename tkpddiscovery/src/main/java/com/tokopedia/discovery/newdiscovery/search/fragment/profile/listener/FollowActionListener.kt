package com.tokopedia.discovery.newdiscovery.search.fragment.profile.listener

interface FollowActionListener {
    fun onSuccessToggleFollow(adapterPosition : Int, enable : Boolean)
    fun onErrorToggleFollow(adapterPosition : Int, errorMessage: String)
    fun getString(resId: Int): String
}
