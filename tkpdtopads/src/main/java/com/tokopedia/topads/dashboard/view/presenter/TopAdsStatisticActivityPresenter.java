package com.tokopedia.topads.dashboard.view.presenter;

import java.util.Date;

/**
 * Created by zulfikarrahman on 1/6/17.
 */
public interface TopAdsStatisticActivityPresenter {

    void getStatisticFromNet(Date startDate, Date endDate, int typeRequest, String shopId);
}
