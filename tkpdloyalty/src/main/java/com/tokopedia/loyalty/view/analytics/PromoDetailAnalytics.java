package com.tokopedia.loyalty.view.analytics;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.loyalty.view.data.PromoData;

import java.util.ArrayList;
import java.util.List;

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
    public void userViewPromo(String promoName, int page, String categoryName, int position,
                              PromoData promoData) {
        List<Object> list = new ArrayList<>();

        list.add(DataLayer.mapOf(
                "id", promoData.getId(),
                "name", "promo detail - P" + String.valueOf(page) + " - " + categoryName,
                "creative", promoData.getThumbnailImage(),
                "position", position,
                "promo_id", "0",
                "promo_code", promoData.isMultiplePromo() ? promoData.getPromoCodeList() : promoData.getPromoCode())
        );

        analyticTracker.sendEventTracking(
                DataLayer.mapOf(
                        "event", EVENT_PROMO_VIEW,
                        "eventCategory", EVENT_CATEGORY_PROMO_DETAIL,
                        "eventAction", EVENT_ACTION_IMPRESSION_PROMO,
                        "eventLabel", promoName,
                        "ecommerce", DataLayer.mapOf(
                                "promoView", DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(
                                                list.toArray(new Object[list.size()]
                                                )
                                        )
                                )
                        )
                )
        );
    }

    @Override
    public void userClickCopyIcon(String promoName) {
        analyticTracker.sendEventTracking(
                new EventTracking(
                        EVENT_PROMO_CLICK_MICROSITE,
                        EVENT_CATEGORY_PROMO_LIST,
                        EVENT_ACTION_COPY_PROMO,
                        promoName
                ).getEventMap());
    }

    @Override
    public void userClickCta(String promoName, int page, String categoryName, int position,
                             PromoData promoData) {

        List<Object> list = new ArrayList<>();
        list.add(DataLayer.mapOf(
                "id", promoData.getId(),
                "name", "promo detail - P" + String.valueOf(page) + " - " + categoryName,
                "creative", promoData.getThumbnailImage(),
                "position", String.valueOf(position + 1),
                "promo_id", "0",
                "promo_code", promoData.isMultiplePromo() ? promoData.getPromoCodeList() : promoData.getPromoCode())
        );

        analyticTracker.sendEventTracking(
                DataLayer.mapOf(
                        "event", EVENT_PROMO_CLICK,
                        "eventCategory", EVENT_CATEGORY_PROMO_DETAIL,
                        "eventAction", EVENT_ACTION_CTA,
                        "eventLabel", promoName,
                        "ecommerce", DataLayer.mapOf(
                                "promoClick", DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(
                                                list.toArray(new Object[list.size()]
                                                )
                                        )
                                )
                        )
                )
        );
    }

    @Override
    public void userSharePromo(String socialMediaName) {
        analyticTracker.sendEventTracking(
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
        analyticTracker.sendEventTracking(
                new EventTracking(
                        EVENT_PROMO_CLICK_MICROSITE,
                        EVENT_CATEGORY_PROMO_TOOLTIP,
                        EVENT_ACTION_TOOLTIP_CLICK,
                        ""
                ).getEventMap());
    }

    @Override
    public void userCloseTooltip() {
        analyticTracker.sendEventTracking(
                new EventTracking(
                        EVENT_PROMO_CLICK_MICROSITE,
                        EVENT_CATEGORY_PROMO_TOOLTIP,
                        EVENT_ACTION_TOOLTIP_CLOSE,
                        ""
                ).getEventMap());
    }
}
