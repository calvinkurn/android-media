package com.tokopedia.seller.topads.view.model;

/**
 * Created by Nathaniel on 2/23/2017.
 */

public interface AdDetailViewModel {

    long getId();

    String getTitle();

    long getShopId();

    int getStatus();

    float getPriceBid();

    float getPriceDaily();

    String getStartDate();

    String getStartTime();

    String getEndDate();

    String getEndTime();

    int getStickerId();
}