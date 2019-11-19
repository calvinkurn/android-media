package com.tokopedia.discovery.newdiscovery.hotlistRevamp.view.interfaces

import com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.cpmAds.CpmItem

interface CpmTopAdsListener {
    fun onCpmClicked(trackerUrl: String, item: CpmItem)
    fun onCpmImpression(item: CpmItem)
}