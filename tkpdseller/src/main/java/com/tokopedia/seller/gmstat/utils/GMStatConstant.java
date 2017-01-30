package com.tokopedia.seller.gmstat.utils;

import com.tokopedia.core.network.constants.TkpdBaseURL;

/**
 * Created by normansyahputa on 11/2/16.
 */

public final class GMStatConstant {
    public static final String GMSTAT_TAG = "GMSTAT_TAG";
    public static final String UPPER_BUYER_FORMAT = "%3d%% %s";
    public static final String LOWER_BUYER_FORMAT = "%3d%%";
    public static final String PERCENTAGE_FORMAT = "%s%%";
    public static final String NUMBER_TIMES_FORMAT = "%sx";
    public static final String MARKET_INSIGHT_FOOTER_FORMAT = "\"<i><b>%s</b></i>\"";
    public static final String LAST_THIRTY_DAYS_AGO_FORMAT = "<i>%s</i>";
    public static final String RANGE_DATE_FORMAT = "%s - %s";
    public static final String SINGLE_DATE_FORMAT = "%s";
    // this is used by automated testing.
    public static String baseUrl = TkpdBaseURL.GOLD_MERCHANT_DOMAIN;
}
