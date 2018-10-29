package com.tokopedia.transaction.purchase.utils;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.transaction.router.ITransactionOrderDetailRouter;
import static com.tokopedia.transaction.purchase.utils.OrderDetailConstant.*;

import java.util.HashMap;

/**
 * Temporary class to provide analytics in tkpdtransaction
 * Will be moved soon when refactoring
 * Will be merged with analytics module
 */
public class OrderDetailAnalytics {

    private AnalyticTracker analyticTracker;
    private Context context;

    public OrderDetailAnalytics(Context context) {
        if (context == null)
            return;

        this.context = context;

        if (context.getApplicationContext() instanceof AbstractionRouter) {
            analyticTracker = ((AbstractionRouter) context.getApplicationContext())
                    .getAnalyticTracker();
        }
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
        analyticTracker.sendEventTracking(createEventMap(event, category, action, label));
    }

    public void sendAnalyticsClickShipping(String action, String label) {
        sendAnalytics(VALUE_CLICK_ORDER,
                VALUE_SALES_SHIPPING,
                action, label);
    }

}
