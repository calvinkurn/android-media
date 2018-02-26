package com.tokopedia.transaction.checkout.util;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;

/**
 * @author Aghny A. Putra on 26/02/18
 */

public class PeopleAddressAuthUtil {

    private static final String PARAM_ORDER_BY = "order_by";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_QUERY = "query";

    /**
     * @param context
     * @param order
     * @param query
     * @param page
     * @return
     */
    public static RequestParams getRequestParams(final Context context,
                                                 final int order,
                                                 final String query,
                                                 final int page) {

        // Get people address list from api requires parameter of order, keyword, and page
        // Create a map from those parameters
        final HashMap<String, String> params = new HashMap<String, String>() {{
            put(PARAM_ORDER_BY, String.valueOf(order));
            put(PARAM_QUERY, query);
            put(PARAM_PAGE, String.valueOf(page));
        }};

        // Create network auth params from plain params using auth util generator,
        // which will retrieve another params such as device id, os type and timestamp
        // and generate a hash based on those particular params
        final HashMap<String, Object> authParams = new HashMap<String, Object>() {{
            putAll(AuthUtil.generateParams(context, params));
        }};

        // Create request params which contains the auth params
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(authParams);

        return requestParams;
    }

}