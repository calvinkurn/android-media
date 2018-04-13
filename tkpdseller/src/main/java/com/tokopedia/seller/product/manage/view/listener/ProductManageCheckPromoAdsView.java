package com.tokopedia.seller.product.manage.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;

/**
 * Created by hadi.putra on 12/04/18.
 */

public interface ProductManageCheckPromoAdsView extends CustomerView {
    void moveToCreateAds();

    void moveToAdsDetail(String adsId);

    void showLoadingProgress();

    void finishLoadingProgress();

    void renderErrorView(String message);

    void renderRetryRefresh();
}
