package com.tokopedia.discovery.intermediary.analytics;

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

public class IntermediaryAnalytics {
    public static final String clickIntermediaryEvent = "clickIntermediary";
    public static final String clickIntermediaryCategory = "intermediary page";
    public static final String clickIntermediaryActionLihatSemuaOs = "click lihat semua on os widget";

    public static void eventClickSeeAllOfficialStores() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    clickIntermediaryEvent,
                    clickIntermediaryCategory,
                    clickIntermediaryActionLihatSemuaOs,
                    ""
            ));

    }
}
