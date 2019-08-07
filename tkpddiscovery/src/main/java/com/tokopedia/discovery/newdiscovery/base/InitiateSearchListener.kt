package com.tokopedia.discovery.newdiscovery.base

interface InitiateSearchListener {

    fun onHandleResponseSearch(isHasCatalog: Boolean)

    fun onHandleApplink(applink: String)

    fun onHandleResponseUnknown()

    fun onHandleResponseError()
}