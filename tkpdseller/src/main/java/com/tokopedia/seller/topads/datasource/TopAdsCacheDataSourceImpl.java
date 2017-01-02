package com.tokopedia.seller.topads.datasource;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nathaniel on 11/25/2016.
 */

public class TopAdsCacheDataSourceImpl implements TopAdsCacheDataSource {

    private static final String REQUEST_DATE_FORMAT = TopAdsConstant.REQUEST_DATE_FORMAT;
    private static final String LOCAL_CACHE_NAME = "top_ads_cache";

    private static final String PARAM_START_DATE = "PARAM_START_DATE";
    private static final String PARAM_END_DATE = "PARAM_END_DATE";

    private LocalCacheHandler localCacheHandler;

    public TopAdsCacheDataSourceImpl(Context context) {
        localCacheHandler = new LocalCacheHandler(context, LOCAL_CACHE_NAME);
    }

    @Override
    public void resetDate() {
        localCacheHandler.putString(PARAM_START_DATE, null);
        localCacheHandler.putString(PARAM_END_DATE, null);
    }

    @Override
    public void saveDate(Date startDate, Date endDate) {
        if (startDate != null) {
            localCacheHandler.putString(PARAM_START_DATE, new SimpleDateFormat(REQUEST_DATE_FORMAT, Locale.ENGLISH).format(startDate));
        }
        if (endDate != null) {
            localCacheHandler.putString(PARAM_END_DATE, new SimpleDateFormat(REQUEST_DATE_FORMAT, Locale.ENGLISH).format(endDate));
        }
    }

    @Override
    public Date getStartDate(Date defaultDate) {
        Date date = defaultDate;
        try {
            date = new SimpleDateFormat(REQUEST_DATE_FORMAT, Locale.ENGLISH).parse(localCacheHandler.getString(PARAM_START_DATE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public Date getEndDate(Date defaultDate) {
        Date date = defaultDate;
        try {
            date = new SimpleDateFormat(REQUEST_DATE_FORMAT, Locale.ENGLISH).parse(localCacheHandler.getString(PARAM_END_DATE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
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