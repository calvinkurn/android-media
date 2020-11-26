package com.tokopedia.seller.shop.common.utils;

import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.core.network.constants.TkpdBaseURL;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by nathan on 9/16/17.
 */

public class ShopOpenCacheApiWhiteList {

    private static final long THREE_HOURS = TimeUnit.HOURS.toSeconds(3);

    public static List<CacheApiWhiteListDomain> getWhiteList() {
        List<CacheApiWhiteListDomain> cacheApiWhiteList = new ArrayList<>();

        // Shop info
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TokopediaUrl.Companion.getInstance().getWS(),
                TkpdBaseURL.Shop.PATH_SHOP + TkpdBaseURL.Shop.PATH_GET_SHOP_INFO, THREE_HOURS));
        // Generate Host
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TokopediaUrl.Companion.getInstance().getWS(),
                TkpdBaseURL.Upload.V4_ACTION_GENERATE_HOST + TkpdBaseURL.Upload.PATH_GENERATE_HOST, THREE_HOURS));

        return cacheApiWhiteList;
    }
}
