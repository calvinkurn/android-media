package com.tokopedia.seller.topads.datasource;

/**
 * Created by Nathaniel on 11/28/2016.
 */

public interface TopAdsCacheDataSource {

    void updateLastInsertStatistic();

    boolean isStatisticDataExpired();
}