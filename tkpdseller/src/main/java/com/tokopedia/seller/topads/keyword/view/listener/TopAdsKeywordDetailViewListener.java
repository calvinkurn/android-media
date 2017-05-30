package com.tokopedia.seller.topads.keyword.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.topads.data.model.data.Ad;
import com.tokopedia.seller.topads.keyword.view.model.KeywordAd;

/**
 * Created by zulfikarrahman on 12/30/16.
 */
public interface TopAdsKeywordDetailViewListener extends CustomerView {

    void onAdLoaded(KeywordAd ad);

    void onLoadAdError();

    void onTurnOnAdSuccess();

    void onTurnOnAdError();

    void onTurnOffAdSuccess();

    void onTurnOffAdError();

    void onDeleteAdSuccess();

    void onDeleteAdError();
}
