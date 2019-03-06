package com.tokopedia.transaction.orders.orderdetails.view;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import javax.inject.Inject;

public class OrderListAnalytics {

    private static final String PRODUCT_EVENT_NAME = "clickPurchaseList";
    private static final String PRODUCT_EVENT_CATEGORY = "my purchase list - mp";
    private static final String PRODUCT_EVENT_ACTION = "click product";
    private static final String FILTER_EVENT_ACTION = "click quick filter";
    private static final String DATE_EVENT_ACTION = "submit date filter";
    private static final String SEARCH_EVENT_ACTION = "submit search";
    private static final String INVOICE_EVENT_ACTION = "click view invoice";
    private static final String LOAD_MORE_EVENT_ACTION = "scroll load more";

    private AnalyticTracker tracker;


    @Inject
    public OrderListAnalytics(@ApplicationContext Context context) {
        if (context != null && context.getApplicationContext() instanceof AbstractionRouter) {
            tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        }
    }

    public void sendProductClickEvent(String eventLabel) {
        tracker.sendEventTracking(PRODUCT_EVENT_NAME, PRODUCT_EVENT_CATEGORY, PRODUCT_EVENT_ACTION, eventLabel);
    }

    public void sendQuickFilterClickEvent(String filterLabel) {
        tracker.sendEventTracking(PRODUCT_EVENT_NAME, PRODUCT_EVENT_CATEGORY, FILTER_EVENT_ACTION, filterLabel);
    }

    public void sendDateFilterClickEvent() {
        tracker.sendEventTracking(PRODUCT_EVENT_NAME, PRODUCT_EVENT_CATEGORY, DATE_EVENT_ACTION, "");
    }

    public void sendSearchFilterClickEvent() {
        tracker.sendEventTracking(PRODUCT_EVENT_NAME, PRODUCT_EVENT_CATEGORY, SEARCH_EVENT_ACTION, "");
    }

    public void sendViewInvoiceClickEvent() {
        tracker.sendEventTracking(PRODUCT_EVENT_NAME, PRODUCT_EVENT_CATEGORY, INVOICE_EVENT_ACTION, "");
    }

    public void sendActionButtonClickEvent(String eventAction) {
        tracker.sendEventTracking(PRODUCT_EVENT_NAME, PRODUCT_EVENT_CATEGORY, eventAction, "");
    }

    public void sendLoadMoreEvent(String eventLabel) {
        tracker.sendEventTracking(PRODUCT_EVENT_NAME, PRODUCT_EVENT_CATEGORY, LOAD_MORE_EVENT_ACTION, eventLabel);
    }
}
