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
    public static final long NO_DATA_AVAILABLE = -2147483600; // constant from server
    // this is used by automated testing.
    public static String baseUrl = TkpdBaseURL.GOLD_MERCHANT_DOMAIN;
}
