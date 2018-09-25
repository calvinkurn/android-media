package com.tokopedia.discovery.intermediary.analytics;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.core.analytics.AppEventTracking;

public class IntermediaryAnalytics {
    public static void eventClickSeeAllOfficialStores(Context context) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        if (tracker != null) {
            tracker.sendEventTracking(
                    "",
                    "intermediary page",
                    "click lihat semua on os widget",
                    ""
            );
        }
    }
}
