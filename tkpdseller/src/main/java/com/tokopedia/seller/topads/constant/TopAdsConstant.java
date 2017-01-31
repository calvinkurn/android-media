package com.tokopedia.seller.topads.constant;

import android.text.format.DateUtils;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsConstant {

    public static final int OFFSCREEN_PAGE_LIMIT = 3;

    public static final String REQUEST_DATE_FORMAT = "yyyy-MM-dd";
    public static final int CACHE_EXPIRED_TIME = (int) (DateUtils.HOUR_IN_MILLIS / DateUtils.SECOND_IN_MILLIS);

    public static final String EXTRA_CREDIT = "EXTRA_CREDIT";

    public static final String EDIT_PROMO_DISPLAY_DATE = "dd/MM/yyyy";
    public static final String EDIT_PROMO_DISPLAY_TIME = "HH:mm a";

    public static final int STATUS_AD_ALL_ACTIVE = 0;
    public static final int STATUS_AD_ACTIVE = 1;
    public static final int STATUS_AD_NOT_ACTIVE = 3;
    public static final int STATUS_AD_NOT_SENT = 2;

    public static final int MAX_DATE_RANGE = 60;
}
