package com.tokopedia.seller.topads.keyword.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.topads.data.model.data.Ad;

/**
 * Created by zulfikarrahman on 12/30/16.
 */
public interface TopAdsKeywordDetailViewListener extends CustomerView {

    void onAdLoaded(Ad ad);

    void onLoadAdError();

    void onTurnOnAdSuccess();

    void onTurnOnAdError();

    void onTurnOffAdSuccess();

    void onTurnOffAdError();

    void onDeleteAdSuccess();

    void onDeleteAdError();
}
