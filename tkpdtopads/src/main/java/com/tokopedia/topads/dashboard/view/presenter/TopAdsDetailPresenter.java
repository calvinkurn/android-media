package com.tokopedia.topads.dashboard.view.presenter;

import java.util.Date;

/**
 * Created by zulfikarrahman on 8/14/17.
 */

public interface TopAdsDetailPresenter extends RetrofitPresenter {
    void refreshAd(Date startDate, Date endDate, String id);
}
