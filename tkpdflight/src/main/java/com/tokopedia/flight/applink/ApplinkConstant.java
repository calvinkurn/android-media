package com.tokopedia.flight.applink;

import com.tokopedia.flight.detail.view.activity.FlightDetailOrderActivity;

/**
 * Created by nathan on 1/8/18.
 */

public class ApplinkConstant {

    public static final String FLIGHT = "tokopedia://flight";
    public static final String FLIGHT_ORDER = "tokopedia://flight/order";
    public static final String FLIGHT_ORDER_DETAIL = "tokopedia://flight/order/{" + FlightDetailOrderActivity.EXTRA_ORDER_PASS_DETAIL + "}";
}
