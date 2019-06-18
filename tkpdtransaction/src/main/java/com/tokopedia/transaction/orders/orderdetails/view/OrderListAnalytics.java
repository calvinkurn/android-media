package com.tokopedia.transaction.orders.orderdetails.view;

import javax.inject.Inject;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

public class OrderListAnalytics {

    private static final String PRODUCT_EVENT_NAME = "clickPurchaseList";
    private static final String PRODUCT_EVENT_CATEGORY = "my purchase list - mp";
    private static final String PRODUCT_EVENT_ACTION = "click product";
    private static final String FILTER_EVENT_ACTION = "click quick filter";
    private static final String DATE_EVENT_ACTION = "submit date filter";
    private static final String SEARCH_EVENT_ACTION = "submit search";
    private static final String INVOICE_EVENT_ACTION = "click view invoice";
    private static final String LOAD_MORE_EVENT_ACTION = "scroll load more";



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
}
