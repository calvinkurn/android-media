package com.tokopedia.seller.topads.dashboard.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.topads.dashboard.view.model.Ad;

/**
 * Created by zulfikarrahman on 12/30/16.
 */
public interface TopAdsDetailViewListener<V extends Ad> extends TopAdsDetailListener<V> {

    void onTurnOnAdSuccess();

    void onTurnOnAdError();

    void onTurnOffAdSuccess();

    void onTurnOffAdError();

    void onDeleteAdSuccess();

    void onDeleteAdError();
}
