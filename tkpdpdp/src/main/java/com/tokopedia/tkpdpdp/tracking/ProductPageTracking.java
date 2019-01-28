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
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef;
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel;
import com.tokopedia.topads.sdk.domain.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nakama on 4/2/18.
 */

public class ProductPageTracking {

    public static final String EVENT = "event";
    public static final String EVENT_CATEGORY = "eventCategory";
    public static final String EVENT_ACTION = "eventAction";
    public static final String EVENT_LABEL = "eventLabel";

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
    public static final String PROMO_BANNER = "promo banner";

    public static final String ECOMMERCE = "ecommerce";
    public static final String PROMO_VIEW = "promoView";
    public static final String PROMO_CLICK = "promoClick";

    public static final String PROMOTIONS = "promotions";
    public static final String NAME = "name";
    public static final String PROMO_ID = "promo_id";
    public static final String PROMO_CODE = "promo_code";
    public static final String ID = "id";
    public static final String POSITION = "position";

    public static final String USER_ID = "user_id";

    public static final String EVENT_CLICK_AFFILIATE = "clickAffiliate";
    public static final String PRODUCT_DETAIL_PAGE_BY_ME = "product detail page tokopedia by.me";
    public static final String ACTION_CLICK_TAMBAH_BY_ME = "click tambah ke by.me";
    public static final String EMPTY_LABEL = "";
    public static final String ACTION_CLICK_WISHLIST = "click wishlist";
    public static final String EVENT_ACTION_CLICK_REVIEW_ON_BUYERS_IMAGE = "click - review gallery on foto dari pembeli";
    public static final String EVENT_LABEL_CLICK_REVIEW_ON_BUYERS_IMAGE = "product_id: %s - review_id : %s";
    public static final String EVENT_ACTION_CLICK_REVIEW_ON_SEE_ALL_IMAGE = "click - lihat semua review gallery";
    public static final String EVENT_ACTION_CLICK_REVIEW_ON_MOST_HELPFUL_REVIEW = "click - review gallery on most helpful review";
    public static final String EVENT_LABEL_CLICK_REVIEW_ON_MOST_HELPFUL_REVIEW = "product_id: %s - review:id : %s";
    public static final String EVENT_ACTION_CLICK_FILTER_REVIEW_BY = "click - filter review by %s";
    public static final String EVENT_ACTION_CLICK_IMAGE_ON_REVIEW_LIST = "click - review gallery on review list";
    public static final String EVENT_LABEL_CLICK_IMAGE_ON_REVIEW_LIST = "product_id: %s - review_id : %s";

    public static final String CLICK_BY_ME = "click - by.me";

    public static final String LABEL_CLICK_BY_ME = "%s - %s";

    private static String joinDash(String... s) {
        return TextUtils.join(" - ", s);
    }

    private static String joinSpace(String... s) {
        return TextUtils.join(" ", s);
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

    public static void eventAppsFlyer(String productId, String priceItem, int quantity, String productName, String category) {
        Map<String, Object> values = new HashMap<>();

        values.put(AFInAppEventParameterName.CONTENT_ID, productId);
        values.put(AFInAppEventParameterName.CURRENCY, "IDR");
        values.put(AFInAppEventParameterName.PRICE,
                CurrencyFormatHelper.convertRupiahToInt(priceItem));
        values.put(AFInAppEventParameterName.QUANTITY, quantity);
        values.put(AFInAppEventParameterName.DESCRIPTION, productName);
        values.put(Jordan.AF_KEY_CATEGORY_NAME, category);

        PaymentTracking.atcAF(MainApplication.getAppContext(), values);
    }

    public static void eventTopAdsClicked(Context context, int position, Product product) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", "productClick",
                        "eventCategory", "product detail page",
                        "eventAction", "click - topads",
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
                        "eventAction", "impression - topads",
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

    private static HashMap<String, Object> createMap(String event, String category, String action, String label) {
        HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put(EVENT, event);
        eventMap.put(EVENT_CATEGORY, category);
        eventMap.put(EVENT_ACTION, action);
        eventMap.put(EVENT_LABEL, label);
        return eventMap;
    }

    private static HashMap<String, Object> createMvcImpressionMap(String event, String category, String action, String label,
                                                                  List<MerchantVoucherViewModel> viewModelList) {
        List<Object> mvcListMap = createMvcListMap(viewModelList, 0);
        if (mvcListMap.size() > 0) {
            HashMap<String, Object> eventMap = createMap(event, category, action, label);
            eventMap.put(ECOMMERCE, DataLayer.mapOf(
                    PROMO_VIEW, DataLayer.mapOf(
                            PROMOTIONS, mvcListMap)));
            return eventMap;
        } else {
            return null;
        }
    }

    private static HashMap<String, Object> createMvcClickMap(String event, String category, String action,
                                                             MerchantVoucherViewModel viewModel, int positionIndex) {
        ArrayList<MerchantVoucherViewModel> viewModelList = new ArrayList<>();
        viewModelList.add(viewModel);
        HashMap<String, Object> eventMap = createMap(event, category, action, viewModel.getVoucherName());
        eventMap.put(ECOMMERCE, DataLayer.mapOf(
                PROMO_CLICK, DataLayer.mapOf(
                        PROMOTIONS, createMvcListMap(viewModelList, positionIndex))));
        return eventMap;
    }

    private static List<Object> createMvcListMap(List<MerchantVoucherViewModel> viewModelList, int startIndex) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < viewModelList.size(); i++) {
            MerchantVoucherViewModel viewModel = viewModelList.get(i);
            if (viewModel.isAvailable()) {
                list.add(
                        DataLayer.mapOf(
                                ID, viewModel.getVoucherId(),
                                NAME, joinDash(viewModel.getVoucherName()),
                                POSITION, String.valueOf(startIndex + i + 1),
                                PROMO_ID, viewModel.getVoucherId(),
                                PROMO_CODE, viewModel.getVoucherCode()
                        )
                );
            }
        }
        if (list.size() == 0) {
            return new ArrayList<>();
        }
        return DataLayer.listOf(list.toArray(new Object[list.size()]));
    }

    public static void eventClickMerchantVoucherUse(Context context, MerchantVoucherViewModel viewModel, int positionIndex) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                createMvcClickMap(PROMO_CLICK,
                        PRODUCT_DETAIL_PAGE,
                        joinSpace(PROMO_BANNER, CLICK),
                        viewModel,
                        positionIndex));
    }

    public static void eventImpressionMerchantVoucherUse(Context context, List<MerchantVoucherViewModel> merchantVoucherViewModelList) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        HashMap<String, Object> map = createMvcImpressionMap(PROMO_VIEW,
                PRODUCT_DETAIL_PAGE,
                joinSpace(PROMO_BANNER, IMPRESSION),
                USE_VOUCHER,
                merchantVoucherViewModelList);
        if (map != null) {
            tracker.sendEventTracking(map);
        }
    }

    public static void eventClickAffiliateRegularPdp(Context context,
                                           String userId,
                                           String shopId,
                                           String productId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        Map<String,Object> eventTracking = new HashMap<>();
        eventTracking.put("event", CLICK_PDP);
        eventTracking.put("eventCategory", PRODUCT_DETAIL_PAGE);
        eventTracking.put("eventAction", CLICK_BY_ME);
        eventTracking.put("eventLabel", String.format(LABEL_CLICK_BY_ME, shopId, productId));
        eventTracking.put(USER_ID, userId);

        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(eventTracking);
    }

    public static void eventClickAffiliate(Context context,
                                           String userId,
                                           String productId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        Map<String,Object> eventTracking = new HashMap<>();
        eventTracking.put("event", EVENT_CLICK_AFFILIATE);
        eventTracking.put("eventCategory", PRODUCT_DETAIL_PAGE_BY_ME);
        eventTracking.put("eventAction", ACTION_CLICK_TAMBAH_BY_ME);
        eventTracking.put("eventLabel", productId);
        eventTracking.put(USER_ID, userId);

        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(eventTracking);
    }

    public static void eventClickWishlistOnAffiliate(Context context,
                                                     String userId,
                                                     String productId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        Map<String,Object> eventTracking = new HashMap<>();
        eventTracking.put("event", EVENT_CLICK_AFFILIATE);
        eventTracking.put("eventCategory", PRODUCT_DETAIL_PAGE_BY_ME);
        eventTracking.put("eventAction", ACTION_CLICK_WISHLIST);
        eventTracking.put("eventLabel", productId);
        eventTracking.put(USER_ID, userId);

        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(eventTracking);
    }

    public static void eventClickReviewOnBuyersImage(Context context,
                                                     String productId,
                                                     String reviewId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker =
                ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                EVENT_ACTION_CLICK_REVIEW_ON_BUYERS_IMAGE,
                String.format(
                        EVENT_LABEL_CLICK_REVIEW_ON_BUYERS_IMAGE,
                        productId,
                        reviewId
                )
        );
    }

    public static void eventClickReviewOnSeeAllImage(Context context, String productId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker =
                ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                EVENT_ACTION_CLICK_REVIEW_ON_SEE_ALL_IMAGE,
                productId
        );
    }

    public static void eventClickReviewOnMostHelpfulReview(Context context,
                                                           String productId,
                                                           String reviewId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker =
                ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                EVENT_ACTION_CLICK_REVIEW_ON_MOST_HELPFUL_REVIEW,
                String.format(
                        EVENT_LABEL_CLICK_REVIEW_ON_MOST_HELPFUL_REVIEW,
                        productId,
                        reviewId
                )
        );
    }

    public static void eventClickFilterReview(Context context,
                                              String filterName,
                                              String productId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker =
                ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                String.format(
                        EVENT_ACTION_CLICK_FILTER_REVIEW_BY,
                        filterName.toLowerCase()
                ),
                productId
        );
    }

    public static void eventClickImageOnReviewList(Context context,
                                                     String productId,
                                                     String reviewId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker =
                ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                EVENT_ACTION_CLICK_IMAGE_ON_REVIEW_LIST,
                String.format(
                        EVENT_LABEL_CLICK_IMAGE_ON_REVIEW_LIST,
                        productId,
                        reviewId
                )
        );
    }
}
