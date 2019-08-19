package com.tokopedia.transaction.orders.orderdetails.view;

import javax.inject.Inject;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OrderListAnalytics {

    private static final String PRODUCT_EVENT_NAME = "clickPurchaseList";
    private static final String PRODUCT_EVENT_CATEGORY = "my purchase list - mp";
    private static final String PRODUCT_EVENT_DETAIL = "my purchase list detail - mp";
    private static final String PRODUCT_EVENT_ACTION = "click product";
    private static final String FILTER_EVENT_ACTION = "click quick filter";
    private static final String DATE_EVENT_ACTION = "submit date filter";
    private static final String SEARCH_EVENT_ACTION = "submit search";
    private static final String INVOICE_EVENT_ACTION = "click view invoice";
    private static final String LOAD_MORE_EVENT_ACTION = "scroll load more";

    private static final String ID = "id";
    private static final String CATEGORY = "deals";
    private static final String NAME = "name";
    private static final String PRICE = "price";
    private static final String EVENT_TRANSACTION = "transaction";
    private static final String EVENT_CARTEGORY = "digital-deals";
    private static final String ACTION = "view purchase attempt";
    private static final String LABEL = "purchase attempt status";
    private static final String CURRENCY_CODE = "currencyCode";
    private static final String IDR = "IDR";
    private static final String QUANTITY = "quantity";
    private static final String REVENUE = "revenue";
    private static final String AFFILIATION = "affiliation";
    private static final String BRAND = "brand";
    private static final String VARIANT = "variant";
    private static final String SHIPPING = "shipping";
    private static final String TAX = "tax";
    private static final String COUPON_CODE = "coupon";
    private static final String KEY_PRODUCTS = "products";
    private static final String KEY_PURCHASE = "purchase";
    private static final String KEY_ACTION_FIELD = "actionField";
    private static final String SCREEN_NAME = "/digital/deals/thanks";
    private static final String ACTION_CLICK_SEE_BUTTON_ON_ATC_SUCCESS_TOASTER = "click lihat button on atc success toaster";


    @Inject
    public OrderListAnalytics() {
    }

    public void sendProductClickEvent(String eventLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(PRODUCT_EVENT_NAME, PRODUCT_EVENT_CATEGORY, PRODUCT_EVENT_ACTION, eventLabel));
    }

    public void sendQuickFilterClickEvent(String filterLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(PRODUCT_EVENT_NAME, PRODUCT_EVENT_CATEGORY, FILTER_EVENT_ACTION, filterLabel));
    }

    public void sendDateFilterClickEvent() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(PRODUCT_EVENT_NAME, PRODUCT_EVENT_CATEGORY, DATE_EVENT_ACTION, ""));
    }

    public void sendSearchFilterClickEvent() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(PRODUCT_EVENT_NAME, PRODUCT_EVENT_CATEGORY, SEARCH_EVENT_ACTION, ""));
    }

    public void sendViewInvoiceClickEvent() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(PRODUCT_EVENT_NAME, PRODUCT_EVENT_CATEGORY, INVOICE_EVENT_ACTION, ""));
    }

    public void sendActionButtonClickEvent(String eventAction) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(PRODUCT_EVENT_NAME, PRODUCT_EVENT_CATEGORY, eventAction, ""));
    }

    public void sendLoadMoreEvent(String eventLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(PRODUCT_EVENT_NAME, PRODUCT_EVENT_CATEGORY, LOAD_MORE_EVENT_ACTION, eventLabel));
    }

    public void sendThankYouEvent(int entityProductId, String entityProductName, int totalTicketPrice, int quantity, String brandName, String orderId) {
        Map<String, Object> products = new HashMap<>();
        Map<String, Object> purchase = new HashMap<>();
        Map<String, Object> ecommerce = new HashMap<>();
        Map<String, Object> actionField = new HashMap<>();

        products.put(ID, String.valueOf(entityProductId));
        products.put(NAME, entityProductName);
        products.put(PRICE, totalTicketPrice);
        products.put("Category", CATEGORY);
        products.put(QUANTITY, quantity);
        products.put(BRAND, brandName);
        products.put(COUPON_CODE, "none");
        products.put(VARIANT, "none");

        actionField.put(ID, String.valueOf(orderId));
        actionField.put(REVENUE, totalTicketPrice);
        actionField.put(AFFILIATION, brandName);
        actionField.put(SHIPPING, "0");
        actionField.put(TAX, "none");
        actionField.put(COUPON_CODE, "none");

        purchase.put(KEY_ACTION_FIELD, actionField);
        ecommerce.put(CURRENCY_CODE, IDR);
        ecommerce.put(KEY_PRODUCTS, Collections.singletonList(products));
        ecommerce.put(KEY_PURCHASE, purchase);

        Map<String, Object> map = new HashMap<>();
        map.put("event", EVENT_TRANSACTION);
        map.put("eventCategory", EVENT_CARTEGORY);
        map.put("eventAction", ACTION);
        map.put("eventLabel", brandName);
        map.put("currentSite", "tokopediadigital");
        map.put("ecommerce", ecommerce);
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(map);
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(SCREEN_NAME);
    }

    public void sendActionClickButtonSeeOnAtcSuccessToasterEvent() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(PRODUCT_EVENT_NAME, PRODUCT_EVENT_DETAIL, ACTION_CLICK_SEE_BUTTON_ON_ATC_SUCCESS_TOASTER, ""));
    }
}
