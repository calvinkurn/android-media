package com.tokopedia.topads.dashboard.constant;

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
    public static final String EDIT_PROMO_DISPLAY_TIME = "hh:mm a";

    public static final int STATUS_AD_ALL_ACTIVE = 0;
    public static final int STATUS_AD_ACTIVE = 1;
    public static final int STATUS_AD_NOT_ACTIVE = 3;
    public static final int STATUS_AD_NOT_SENT = 2;

    public static final String EMPTY_AD_ID = "0";

    public static final int MAX_DATE_RANGE = 60;

    public static final int BUDGET_MAX = 2000;
    public static final int BUDGET_MULTIPLE_BY = 50;
    public static final int BUDGET_MIN_MULTIPLE_BY = 10;

    public static final int AD_TYPE_PRODUCT = 1;

    public static final int REQUEST_CODE_AD_EDIT = 1;

    public static final int KEYWORD_TYPE_POSITIVE_VALUE = 1;
    public static final int KEYWORD_TYPE_NEGATIVE_VALUE = 0;
}
