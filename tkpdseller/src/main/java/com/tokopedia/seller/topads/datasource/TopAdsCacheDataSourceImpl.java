package com.tokopedia.seller.topads.datasource;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.seller.topads.constant.TopAdsConstant;

/**
 * Created by Nathaniel on 11/25/2016.
 */

public class TopAdsCacheDataSourceImpl implements TopAdsCacheDataSource {

    private static final String LOCAL_CACHE_NAME = "top_ads_cache";

    private LocalCacheHandler localCacheHandler;

    public TopAdsCacheDataSourceImpl(Context context) {
        localCacheHandler = new LocalCacheHandler(context, LOCAL_CACHE_NAME);
    }

    @Override
    public void updateLastInsertStatistic() {
        localCacheHandler.setExpire(TopAdsConstant.CACHE_EXPIRED_TIME);
    }

    @Override
    public boolean isStatisticDataExpired() {
        return localCacheHandler.isExpired();
    }
}