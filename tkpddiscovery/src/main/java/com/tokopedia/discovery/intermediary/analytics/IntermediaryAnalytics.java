package com.tokopedia.discovery.intermediary.analytics;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.core.analytics.AppEventTracking;

public class IntermediaryAnalytics {
    public static final String clickIntermediaryEvent = "clickIntermediary";
    public static final String clickIntermediaryCategory = "intermediary page";
    public static final String clickIntermediaryActionLihatSemuaOs = "click lihat semua on os widget";

    public static void eventClickSeeAllOfficialStores(Context context) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        if (tracker != null) {
            tracker.sendEventTracking(
                    clickIntermediaryEvent,
                    clickIntermediaryCategory,
                    clickIntermediaryActionLihatSemuaOs,
                    ""
            );
        }
    }
}
