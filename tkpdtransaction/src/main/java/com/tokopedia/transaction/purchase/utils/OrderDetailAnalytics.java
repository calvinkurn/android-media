package com.tokopedia.transaction.purchase.utils;

import com.tokopedia.transaction.router.ITransactionOrderDetailRouter;
import static com.tokopedia.transaction.purchase.utils.OrderDetailConstant.*;

import java.util.HashMap;

/**
 * Temporary class to provide analytics in tkpdtransaction
 * Will be moved soon when refactoring
 * Will be merged with analytics module
 */
public class OrderDetailAnalytics {

    private ITransactionOrderDetailRouter router;

    public OrderDetailAnalytics(ITransactionOrderDetailRouter router) {
        this.router = router;
    }

    private HashMap<String, Object> createEventMap(String event, String category, String action, String label) {
        HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put(EVENT, event);
        eventMap.put(EVENT_CATEGORY, category);
        eventMap.put(EVENT_ACTION, action);
        eventMap.put(EVENT_LABEL, label);
        return eventMap;
    }

    public void sendAnalytics(String event, String category, String action, String label) {
        router.sendEventTrackingOrderDetail(createEventMap(event, category, action, label));
    }

    public void sendAnalyticsClickShipping(String action, String label) {
        sendAnalytics(VALUE_CLICK_ORDER,
                VALUE_SALES_SHIPPING,
                action, label);
    }

}
