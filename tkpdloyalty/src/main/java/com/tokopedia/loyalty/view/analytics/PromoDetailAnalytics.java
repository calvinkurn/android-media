package com.tokopedia.loyalty.view.analytics;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import java.util.ArrayList;

import javax.inject.Inject;

public class PromoDetailAnalytics implements IPromoDetailAnalytics {

    private static final String EVENT_PROMO_VIEW = "promoView";
    private static final String EVENT_PROMO_CLICK = "promoClick";
    private static final String EVENT_PROMO_CLICK_MICROSITE = "clickPromoMicrosite";

    private static final String EVENT_CATEGORY_PROMO_LIST = "promo microsite - promo list";
    private static final String EVENT_CATEGORY_PROMO_DETAIL = "promo microsite - promo detail";
    private static final String EVENT_CATEGORY_PROMO_SHARING = "promo microsite - promo sharing";
    private static final String EVENT_CATEGORY_PROMO_TOOLTIP = "promo microsite - promo tooltip";

    private static final String EVENT_ACTION_IMPRESSION_PROMO = "impression on promo";
    private static final String EVENT_ACTION_COPY_PROMO = "user click copy icon";
    private static final String EVENT_ACTION_CTA = "user click belanja sekarang";
    private static final String EVENT_ACTION_SHARE = "user click on social media";
    private static final String EVENT_ACTION_TOOLTIP_CLICK = "user click on tooltip";
    private static final String EVENT_ACTION_TOOLTIP_CLOSE = "user click tutup";

    private AnalyticTracker analyticTracker;

    @Inject
    public PromoDetailAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    @Override
    public void userViewPromo(String promoName, final String id, final int page, final int position,
                              final String creative, final String creativeUrl, final String promoCode) {

        this.analyticTracker.sendEventTracking(
                new EventTracking(
                        EVENT_PROMO_VIEW,
                        EVENT_CATEGORY_PROMO_DETAIL,
                        EVENT_ACTION_IMPRESSION_PROMO,
                        promoName,
                        new Ecommerce(
                                new PromoView(
                                        new ArrayList<Object>() {{
                                            add((new Promotion(
                                                    id,
                                                    page,
                                                    position,
                                                    creative,
                                                    creativeUrl,
                                                    promoCode)
                                            ).getEventMap());
                                        }}
                                )
                        )
                ).getEventMap()
        );
    }

    @Override
    public void userClickCopyIcon(String promoName) {
        this.analyticTracker.sendEventTracking(
                new EventTracking(
                        EVENT_PROMO_CLICK_MICROSITE,
                        EVENT_CATEGORY_PROMO_LIST,
                        EVENT_ACTION_COPY_PROMO,
                        promoName
                ).getEventMap());
    }

    @Override
    public void userClickCta(String promoName, final String id, final int page, final int position,
                             final String creative, final String creativeUrl, final String promoCode) {

        this.analyticTracker.sendEventTracking(
                new EventTracking(
                        EVENT_PROMO_CLICK,
                        EVENT_CATEGORY_PROMO_DETAIL,
                        EVENT_ACTION_CTA,
                        promoName,
                        new Ecommerce(
                                new PromoClick(
                                        new ArrayList<Object>() {{
                                            add((new Promotion(
                                                    id,
                                                    page,
                                                    position,
                                                    creative,
                                                    creativeUrl,
                                                    promoCode)
                                            ).getEventMap());
                                        }}
                                )
                        )
                ).getEventMap()
        );
    }

    @Override
    public void userSharePromo(String socialMediaName) {
        this.analyticTracker.sendEventTracking(
                new EventTracking(
                        EVENT_PROMO_CLICK_MICROSITE,
                        EVENT_CATEGORY_PROMO_SHARING,
                        EVENT_ACTION_SHARE,
                        socialMediaName
                ).getEventMap()
        );
    }

    @Override
    public void userClickTooltip() {
        this.analyticTracker.sendEventTracking(
                new EventTracking(
                        EVENT_PROMO_CLICK_MICROSITE,
                        EVENT_CATEGORY_PROMO_TOOLTIP,
                        EVENT_ACTION_TOOLTIP_CLICK,
                        ""
                ).getEventMap());
    }

    @Override
    public void userCloseTooltip() {
        this.analyticTracker.sendEventTracking(
                new EventTracking(
                        EVENT_PROMO_CLICK_MICROSITE,
                        EVENT_CATEGORY_PROMO_TOOLTIP,
                        EVENT_ACTION_TOOLTIP_CLOSE,
                        ""
                ).getEventMap());
    }
}
