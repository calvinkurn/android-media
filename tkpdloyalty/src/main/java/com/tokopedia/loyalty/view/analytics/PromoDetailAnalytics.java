package com.tokopedia.loyalty.view.analytics;

import javax.inject.Inject;

public class PromoDetailAnalytics implements IPromoDetailAnalytics {

    private static final String EVENT_CATEGORY_PROMO_DETAIL = "promo-microsite - promo detail";
    private static final String EVENT_CATEGORY_PROMO_SHARING = "promo-microsite - promo sharing";
    private static final String EVENT_CATEGORY_PROMO_TOOLTIP = "promo-microsite - promo tooltip";

    private static final String EVENT_ACTION_IMPRESSION_PROMO = "impression on promo";
    private static final String EVENT_ACTION_COPY_PROMO = "user click copy icon";
    private static final String EVENT_ACTION_CTA = "user click belanja sekarang";
    private static final String EVENT_ACTION_SHARE = "user click on social media";
    private static final String EVENT_ACTION_TOOLTIP_CLICK = "user click on tooltip";
    private static final String EVENT_ACTION_TOOLTIP_CLOSE = "user click tutup";

    @Inject
    public PromoDetailAnalytics() {

    }

    @Override
    public void userViewPromo() {

    }

    @Override
    public void userClickCopyIcon() {

    }

    @Override
    public void userClickCta() {

    }

    @Override
    public void userSharePromo() {

    }

    @Override
    public void userClickTooltip() {

    }

    @Override
    public void userCloseTooltip() {

    }
}
