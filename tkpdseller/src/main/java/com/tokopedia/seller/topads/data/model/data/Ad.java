package com.tokopedia.seller.topads.data.model.data;

/**
 * Created by Nathaniel on 12/28/2016.
 */

public interface Ad {

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
}
