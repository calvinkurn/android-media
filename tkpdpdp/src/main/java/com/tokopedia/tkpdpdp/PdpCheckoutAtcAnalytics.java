package com.tokopedia.tkpdpdp;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import java.util.Map;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 17/05/18.
 */
public class PdpCheckoutAtcAnalytics {
    public interface EventName {
        String CLICK_ATC = "clickATC";
    }

    public interface EventCategory {
        String ADD_TO_CART = "add to cart";
    }

    public interface EventAction {
        String IMPRESSION_ATC_SUCCESS = "impression atc success";
        String CLICK_BAYAR_ON_ATC_SUCCESS = "click bayar on atc success";
        String CLICK_LANJUTKAN_BELANJA_ON_ATC_SUCCESS = "click lanjutkan belanja on atc success";
    }

    public interface EventLabel {
        String CLICK_BELI = "click beli";
    }

    private final AnalyticTracker analyticTracker;

    @Inject
    public PdpCheckoutAtcAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public void enhancedECommerceAddToCart(Map<String, Object> cartMap) {
        if (analyticTracker != null)
            analyticTracker.sendEnhancedEcommerce(
                    DataLayer.mapOf("event", "addToCart",
                            "eventCategory", "",
                            "eventAction", EventCategory.ADD_TO_CART,
                            "eventLabel", EventLabel.CLICK_BELI,
                            "ecommerce", cartMap)
            );
    }

    public void eventClickAddToCartImpressionAtcSuccess() {
        if (analyticTracker != null)
            analyticTracker.sendEventTracking(EventName.CLICK_ATC,
                    EventCategory.ADD_TO_CART,
                    EventAction.IMPRESSION_ATC_SUCCESS,
                    ""
            );
    }

    public void eventClickAddToCartClickBayarOnAtcSuccess() {
        if (analyticTracker != null)
            analyticTracker.sendEventTracking(EventName.CLICK_ATC,
                    EventCategory.ADD_TO_CART,
                    EventAction.CLICK_BAYAR_ON_ATC_SUCCESS,
                    ""
            );
    }

    public void eventClickAddToCartClickLanjutkanBelanjaOnAtcSuccess() {
        if (analyticTracker != null)
            analyticTracker.sendEventTracking(EventName.CLICK_ATC,
                    EventCategory.ADD_TO_CART,
                    EventAction.CLICK_LANJUTKAN_BELANJA_ON_ATC_SUCCESS,
                    ""
            );
    }
}
