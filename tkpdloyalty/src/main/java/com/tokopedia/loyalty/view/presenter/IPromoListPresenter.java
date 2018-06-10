package com.tokopedia.loyalty.view.presenter;

import com.tokopedia.loyalty.view.data.PromoData;

import java.util.List;

/**
 * @author anggaprasetiyo on 02/01/18.
 */

public interface IPromoListPresenter {

    void processGetPromoList(String subCategories, String categoryName);

    void processGetPromoListLoadMore(String subCategories, String categoryName);

    void sendImpressionTrackingData(List<PromoData> promoDataList, String categoryName);

    void sendClickItemPromoListTrackingData(PromoData promoData, int position, String categoryName);

    void setPage(int page);

    int getPage();
}
