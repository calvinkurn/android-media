package com.tokopedia.seller.seller.info.view;


import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.track.TrackApp;

public class SellerInfoTracking {
    public static final String SELLER_INFO = "clickSellerInfo";
    public static final String SELLER_INFO_HOMEPAGE = "seller info-homepage";

    public static void eventClickItemSellerInfo(String label){
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                SELLER_INFO,
                SELLER_INFO_HOMEPAGE,
                "click article",
                label
        ).getEvent());
    }

}
