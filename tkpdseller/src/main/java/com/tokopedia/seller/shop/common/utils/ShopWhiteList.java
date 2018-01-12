package com.tokopedia.seller.shop.common.utils;

import com.tokopedia.core.cache.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.core.network.constants.TkpdBaseURL;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by nathan on 9/16/17.
 */

public class ShopWhiteList {

    private static final long TEN_SECOND = TimeUnit.SECONDS.toSeconds(10);
    private static final long THIRTY_SECOND = TimeUnit.SECONDS.toSeconds(30);
    private static final long ONE_MINUTE = TimeUnit.MINUTES.toSeconds(1);
    private static final long FIVE_MINUTE = TimeUnit.MINUTES.toSeconds(5);
    private static final long FIFTEEN_MINUTE = TimeUnit.MINUTES.toSeconds(15);
    private static final long ONE_HOUR = TimeUnit.HOURS.toSeconds(1);
    private static final long THREE_HOURS = TimeUnit.HOURS.toSeconds(3);
    private static final long ONE_DAY = TimeUnit.HOURS.toSeconds(24);

    public static List<CacheApiWhiteListDomain> getWhiteList() {
        List<CacheApiWhiteListDomain> cacheApiWhiteList = new ArrayList<>();

        // Shop info
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.BASE_DOMAIN,
                TkpdBaseURL.Shop.PATH_SHOP + TkpdBaseURL.Shop.PATH_GET_SHOP_INFO, THREE_HOURS));
        // Open Shop form
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.BASE_DOMAIN,
                TkpdBaseURL.Shop.PATH_MY_SHOP + TkpdBaseURL.Shop.PATH_GET_OPEN_SHOP_FORM, ONE_HOUR));
        // Generate Host
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.BASE_DOMAIN,
                TkpdBaseURL.Upload.V4_ACTION_GENERATE_HOST + TkpdBaseURL.Upload.PATH_GENERATE_HOST, THREE_HOURS));

        return cacheApiWhiteList;
    }
}
