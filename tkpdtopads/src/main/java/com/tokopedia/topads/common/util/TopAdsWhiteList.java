package com.tokopedia.topads.common.util;

import com.tokopedia.core.cache.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.seller.opportunity.data.constant.OpportunityConstant;
import com.tokopedia.seller.product.variant.data.cloud.api.TomeApi;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by nathan on 12/28/17.
 */

public class TopAdsWhiteList {

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

        // TopAds Deposit
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.TOPADS_DOMAIN,
                TopAdsNetworkConstant.PATH_DASHBOARD_DEPOSIT, TEN_SECOND));

        // TopAds Credit
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.TOPADS_DOMAIN,
                TopAdsNetworkConstant.PATH_DASHBOARD_CREDIT, ONE_HOUR));

        // TopAds Statistic
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.TOPADS_DOMAIN,
                TopAdsNetworkConstant.PATH_DASHBOARD_STATISTIC, FIVE_MINUTE));

        // opportunity category
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.BASE_DOMAIN, OpportunityConstant.PATH_GET_CATEGORY, ONE_DAY));

        // Suggestion Bid
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.TOPADS_DOMAIN,
                TopAdsNetworkConstant.GET_SUGGESTION, ONE_HOUR));

        return cacheApiWhiteList;
    }
}
