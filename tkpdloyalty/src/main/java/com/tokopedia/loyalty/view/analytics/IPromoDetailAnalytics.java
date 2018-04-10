package com.tokopedia.loyalty.view.analytics;

public interface IPromoDetailAnalytics {

    void userViewPromo(String promoName, String id, int page, int position, String creative,
                       String creativeUrl, String promoCode);

    void userClickCopyIcon(String promoName);

    void userClickCta(String promoName, String id, int page, int position, String creative,
                      String creativeUrl, String promoCode);

    void userSharePromo(String socialMediaName);

    void userClickTooltip();

    void userCloseTooltip();

}
