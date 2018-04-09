package com.tokopedia.loyalty.view.analytics;

import com.tokopedia.loyalty.view.data.PromoData;

public interface IPromoDetailAnalytics {

    void userViewPromo(String promoName, int page, String categoryName, int position,
                       PromoData promoData);

    void userClickCopyIcon(String promoName);

    void userClickCta(String promoName, int page, String categoryName, int position,
                      PromoData promoData);

    void userSharePromo(String socialMediaName);

    void userClickTooltip();

    void userCloseTooltip();

}
