package com.tokopedia.discovery.newdiscovery.hotlistRevamp.domain.mapper

import com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.cpmAds.CpmItem
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.cpmAds.CpmTopAdsResponse

class CpmItemMapper {

    fun transform(cpmTopAdsResponse: CpmTopAdsResponse): List<CpmItem> {

        val list = ArrayList<CpmItem>()

        if (cpmTopAdsResponse.displayAdsV3?.data?.size ?: 0 > 0) {
            val applink = cpmTopAdsResponse.displayAdsV3?.data?.get(0)?.applinks
            val adClickUrl = cpmTopAdsResponse.displayAdsV3?.data?.get(0)?.adClickUrl
            var badge: String? = ""
            if (cpmTopAdsResponse.displayAdsV3?.data?.get(0)?.headline?.badges?.size ?: 0 > 0) {
                badge = cpmTopAdsResponse.displayAdsV3?.data?.get(0)?.headline?.badges?.get(0)?.imageUrl
                        ?: ""
            }
            cpmTopAdsResponse.displayAdsV3?.data?.get(0)?.headline?.let { headline ->
                list.add(CpmItem(headline.name,
                        headline.description,
                        headline.shop?.imageShop?.xsUrl,
                        "",
                        false,
                        applink,
                        adClickUrl,
                        badge))

                headline.shop?.product?.let {

                    for (i in it) {
                        i?.let { productItem ->
                            list.add(CpmItem(productItem.name,
                                    "",
                                    productItem.imageProduct?.imageUrl,
                                    productItem.priceFormat,
                                    true,
                                    productItem.applinks,
                                    productItem.imageProduct?.imageClickUrl,
                                    ""))
                        }
                    }
                }
            }
        }
        return list
    }
}