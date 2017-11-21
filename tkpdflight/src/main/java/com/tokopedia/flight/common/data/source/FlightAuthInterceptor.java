package com.tokopedia.flight.common.data.source;

import com.tokopedia.abstraction.common.network.interceptor.TkpdBaseInterceptor;
import com.tokopedia.abstraction.utils.GlobalConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 11/16/17.
 */

public class FlightAuthInterceptor extends TkpdBaseInterceptor {

    private static final String CONTENT_TYPE = "";

    @Inject
    public FlightAuthInterceptor() {
    }
}
