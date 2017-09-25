package com.tokopedia.topads.dashboard.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.base.view.adapter.ItemType;

/**
 * Created by Nathaniel on 12/28/2016.
 */

public interface Ad extends ItemType, Parcelable {

    String getId();

    int getStatus();

    String getStatusDesc();

    int getStatusToogle();

    String getPriceBidFmt();

    String getPriceDailyFmt();

    String getPriceDailySpentFmt();

    String getPriceDailyBar();

    String getStartDate();

    String getStartTime();

    String getEndDate();

    String getEndTime();

    String getStatAvgClick();

    String getStatTotalSpent();

    String getStatTotalImpression();

    String getStatTotalClick();

    String getStatTotalCtr();

    String getStatTotalConversion();

    String getLabelEdit();

    String getLabelPerClick();

    String getLabelOf();

    String getName();

    @Override
    int describeContents();

    @Override
    void writeToParcel(Parcel parcel, int i);

    @Override
    int getType();
}
