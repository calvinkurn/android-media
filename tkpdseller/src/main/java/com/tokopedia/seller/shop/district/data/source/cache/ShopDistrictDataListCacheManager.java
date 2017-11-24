package com.tokopedia.seller.shop.district.data.source.cache;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.seller.base.data.source.cache.DataListCacheManager;

import java.util.concurrent.TimeUnit;

/**
 * Created by nathan on 10/23/17.
 */

public class ShopDistrictDataListCacheManager extends DataListCacheManager {

    private static final String PREF_KEY_NAME = "PREF_KEY_DISTRICT_LIST";
    private static final long ONE_DAY = TimeUnit.HOURS.toSeconds(24);

    public ShopDistrictDataListCacheManager(@ApplicationContext Context context) {
        super(context);
    }

    @Override
    protected String getPrefKeyName() {
        return PREF_KEY_NAME;
    }

    @Override
    protected long getExpiredTimeInSec() {
        return ONE_DAY;
    }
}
