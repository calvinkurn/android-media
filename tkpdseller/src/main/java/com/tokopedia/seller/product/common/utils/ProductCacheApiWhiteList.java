package com.tokopedia.seller.product.common.utils;

import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.seller.product.variant.data.cloud.api.TomeApi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by nathan on 12/28/17.
 */

public class ProductCacheApiWhiteList {

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

        // Product Recommended category
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.MERLIN_DOMAIN,
                TkpdBaseURL.Merlin.PATH_CATEGORY_RECOMMENDATION, ONE_DAY));
        // Product variant by category
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.TOME_DOMAIN,
                TomeApi.GET_VARIANT_BY_CAT_PATH, ONE_DAY));
        // Product catalog
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.ACE_DOMAIN,
                TkpdBaseURL.Ace.PATH_SEARCH + TkpdBaseURL.Ace.PATH_CATALOG, ONE_DAY));

        return cacheApiWhiteList;
    }
}
