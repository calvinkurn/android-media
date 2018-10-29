package com.tokopedia.tkpdpdp.tracking;

import android.content.Context;
import android.text.TextUtils;

import com.appsflyer.AFInAppEventParameterName;
import com.google.android.gms.tagmanager.DataLayer;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.core.analytics.PaymentTracking;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.topads.sdk.domain.model.Product;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nakama on 4/2/18.
 */

public class ProductPageTracking {

    public static final String CLICK = "click";
    public static final String IMPRESSION = "impression";
    public static final String VIEW_PDP = "viewPDP";
    public static final String CLICK_PDP = "clickPDP";
    public static final String LANDSCAPE_VIEW = "landscape view";
  
    public static final String PRODUCT_DETAIL_PAGE = "product detail page";
    public static final String CLICK_OS_PROMO = "clickOSPromo";
    public static final String PDP_PROMO_WIDGET_PROMO = "pdp promo widget - promo";
    public static final String USER_CLICK_ON_COPY_CODE = "user click on copy code";
    public static final String NAME_PRODUCT_PROMO_WIDGET = "/product - promo widget";
    public static final String POSITION_PDP_WIDGET = "PDP - Widget";
    public static final String CREATIVE_URL_PROMO_WIDGET = "tokopedia.com/creative.png";
    public static final String PDP_PROMO_CLICK_ON_PROMO_SHORT_DESC = "user click on promo short desc";
    public static final String PDP_PROMO_IMPRESSION_EVENT_ACTION = "user impression on promo";
    public static final String EVENT_CATEGORY_PROMO_PDP = "pdp promo widget - promo";

    // MERCHANT VOUCHER
    public static final String MERCHANT_VOUCHER = "merchant voucher";
    public static final String SEE_ALL = "see all";
    public static final String MVC_DETAIL = "mvc detail";
    public static final String USE_VOUCHER = "use voucher";

    private static String joinDash(String... s) {
        return TextUtils.join(" - ", s);
    }

    public static void eventEnhanceProductDetail(Context context, Map<String, Object> maps) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEnhancedEcommerce(maps);
    }

    public static void eventClickAtcNotLogin(Context context, String productId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                "click - tambah ke keranjang - before login",
                productId
        );
    }

    public static void eventClickBuyNotLogin(Context context, String productId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                "click - beli - before login",
                productId
        );
    }

    public static void eventClickBuyInVariantNotLogin(Context context, String productId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                "click - beli on variants page - before login",
                productId
        );
    }

    public static void eventClickAtcInVariantNotLogin(Context context, String productId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                "click - tambah ke keranjang on variants page - before login",
                productId
        );
    }

    public static void eventClickBuyTriggerVariant(Context context, String productId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                "click - beli - redirect to variants page",
                productId
        );
    }

    public static void eventClickAtcTriggerVariant(Context context, String productId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                "click - tambah ke keranjang - redirect to variants page",
                productId
        );
    }

    public static void eventImpressionWidgetPromo(Context context,
                                                  String creative,
                                                  String promoId,
                                                  String promoCode) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", "promoView",
                        "eventCategory", EVENT_CATEGORY_PROMO_PDP,
                        "eventAction", PDP_PROMO_IMPRESSION_EVENT_ACTION,
                        "eventLabel", promoCode,
                        "ecommerce", DataLayer.mapOf("promoView",
                                DataLayer.mapOf("promotions",
                                        DataLayer.listOf(
                                                DataLayer.mapOf(
                                                        "name", NAME_PRODUCT_PROMO_WIDGET,
                                                        "creative", creative,
                                                        "creative_url", CREATIVE_URL_PROMO_WIDGET,
                                                        "position", POSITION_PDP_WIDGET,
                                                        "promo_id", promoId,
                                                        "promo_code", !TextUtils.isEmpty(promoCode) ? promoCode : "NoPromoCode"
                                                )
                                        )
                                )
                        )
                )
        );
    }

    public static void eventClickWidgetPromo(Context context,
                                             String creative,
                                             String promoId,
                                             String promoCode) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", "promoClick",
                        "eventCategory", EVENT_CATEGORY_PROMO_PDP,
                        "eventAction", PDP_PROMO_CLICK_ON_PROMO_SHORT_DESC,
                        "eventLabel", promoCode,
                        "ecommerce", DataLayer.mapOf("promoClick",
                                DataLayer.mapOf("promotions",
                                        DataLayer.listOf(
                                                DataLayer.mapOf(
                                                        "name", NAME_PRODUCT_PROMO_WIDGET,
                                                        "creative", creative,
                                                        "creative_url", CREATIVE_URL_PROMO_WIDGET,
                                                        "position", POSITION_PDP_WIDGET,
                                                        "promo_id", promoId,
                                                        "promo_code", !TextUtils.isEmpty(promoCode) ? promoCode : "NoPromoCode"
                                                )
                                        )
                                )
                        )
                )
        );
    }

    public static void eventClickCopyWidgetPromo(Context context,
                                                 String promoCode) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_OS_PROMO,
                PDP_PROMO_WIDGET_PROMO,
                USER_CLICK_ON_COPY_CODE,
                promoCode
        );
    }

    public static void eventAppsFlyer(String productId, String priceItem, int quantity,String productName, String category) {
        Map<String, Object> values = new HashMap<>();

        values.put(AFInAppEventParameterName.CONTENT_ID, productId);
        values.put(AFInAppEventParameterName.CURRENCY, "IDR");
        values.put(AFInAppEventParameterName.PRICE,
                CurrencyFormatHelper.convertRupiahToInt(priceItem));
        values.put(AFInAppEventParameterName.QUANTITY, quantity);
        values.put(AFInAppEventParameterName.DESCRIPTION,productName);
        values.put(Jordan.AF_KEY_CATEGORY_NAME,category);

        PaymentTracking.atcAF(values);
    }

    public static void eventTopAdsClicked(Context context, int position, Product product) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", "productClick",
                        "eventCategory", "product detail page",
                        "eventAction", "click - top ads",
                        "eventLabel", "",
                        "ecommerce", DataLayer.mapOf("click",
                                DataLayer.mapOf("actionField", DataLayer.mapOf("list", "/productdetail - top ads'"),
                                        "products", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "name", product.getName(),
                                                "id", product.getId(),
                                                "price", product.getPriceFormat(),
                                                "brand", "none / other",
                                                "category", product.getCategory().getId(),
                                                "variant", "none / other",
                                                "position", (position + 1)
                                        )
                                ))
                        )
                )
        );
    }

    public static void eventTopAdsImpression(Context context, int position, Product product) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", "productView",
                        "eventCategory", "product detail page",
                        "eventAction", "impression - top ads",
                        "eventLabel", "",
                        "ecommerce", DataLayer.mapOf("currencyCode", "IDR",
                                "impression", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "name", product.getName(),
                                                "id", product.getId(),
                                                "price", product.getPriceFormat(),
                                                "brand", "none / other",
                                                "category", product.getCategory().getId(),
                                                "variant", "none / other",
                                                "position", (position + 1)
                                        )
                                )
                        )
                )
        );
    }

    public static void eventPdpOrientationChanged(Context context, String productId) {
        if (context != null
                && context.getApplicationContext() != null
                && context.getApplicationContext() instanceof AbstractionRouter) {
            AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
            tracker.sendEventTracking(
                    VIEW_PDP,
                    PRODUCT_DETAIL_PAGE,
                    LANDSCAPE_VIEW,
                    String.format("product_id : %s", productId)
            );
        }
    }

    public static void eventClickMerchantVoucherSeeAll(Context context, String productId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                joinDash(CLICK, MERCHANT_VOUCHER, SEE_ALL),
                productId
        );
    }

    public static void eventClickMerchantVoucherSeeDetail(Context context, String productId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                joinDash(CLICK, MERCHANT_VOUCHER, MVC_DETAIL),
                productId
        );
    }

    public static void eventClickMerchantVoucherUse(Context context, String productId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                joinDash(CLICK, MERCHANT_VOUCHER, USE_VOUCHER),
                productId
        );
    }

    public static void eventImpressionMerchantVoucherUse(Context context, String productId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                VIEW_PDP,
                PRODUCT_DETAIL_PAGE,
                joinDash(IMPRESSION, MERCHANT_VOUCHER, USE_VOUCHER),
                productId
        );
    }
}
