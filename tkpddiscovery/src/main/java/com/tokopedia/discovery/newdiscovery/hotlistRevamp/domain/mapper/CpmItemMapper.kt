package com.tokopedia.discovery.newdiscovery.hotlistRevamp.domain.mapper

import com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.cpmAds.CpmItem
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.cpmAds.CpmTopAdsResponse

class CpmItemMapper {

    fun transform(cpmTopAdsResponse: CpmTopAdsResponse): List<CpmItem> {

        val list = ArrayList<CpmItem>()
        val applink = cpmTopAdsResponse.displayAdsV3?.data?.get(0)?.applinks
        val adClickUrl = cpmTopAdsResponse.displayAdsV3?.data?.get(0)?.adClickUrl
        cpmTopAdsResponse.displayAdsV3?.data?.get(0)?.headline?.let { headline ->
            list.add(CpmItem(headline.name,
                    headline.description,
                    headline.shop?.imageShop?.xsUrl,
                    "",
                    false,
                    applink,
                    adClickUrl))

            headline.shop?.product?.let {

                for (i in it) {
                    i?.let { productItem ->
                        list.add(CpmItem(productItem.name,
                                "",
                                productItem.imageProduct?.imageUrl,
                                productItem.priceFormat,
                                true,
                                productItem.applinks,
                                productItem.imageProduct?.imageClickUrl))

                    }
                }

            }

        }
        return list
    }
}