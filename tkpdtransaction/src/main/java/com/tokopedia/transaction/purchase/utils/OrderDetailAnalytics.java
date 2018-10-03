package com.tokopedia.transaction.purchase.utils;

import com.tokopedia.transaction.router.ITransactionOrderDetailRouter;

import java.util.HashMap;

public class OrderDetailAnalytics {

    ITransactionOrderDetailRouter router;

    public OrderDetailAnalytics(ITransactionOrderDetailRouter router) {
        this.router = router;
    }

    private HashMap<String, Object> createEventMap(String event, String category, String action, String label) {
        HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put(OrderDetailConstant.EVENT, event);
        eventMap.put(OrderDetailConstant.EVENT_CATEGORY, category);
        eventMap.put(OrderDetailConstant.EVENT_ACTION, action);
        eventMap.put(OrderDetailConstant.EVENT_LABEL, label);
        return eventMap;
    }

    public void sendAnalytics(String event, String category, String action, String label) {
        router.sendEventTrackingOrderDetail(createEventMap(event, category, action, label));
    }

    public void sendAnalyticsClickShipping(String action, String label) {
        sendAnalytics(OrderDetailConstant.VALUE_CLICK_ORDER,
                OrderDetailConstant.VALUE_SALES_SHIPPING,
                action, label);
    }

}
