package com.tokopedia.transaction.checkout.util;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Aghny A. Putra on 26/02/18
 */

public class PeopleAddressAuthUtil {

    private static final String PARAM_ORDER_BY = "order_by";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_QUERY = "query";

    /**
     *
     * @param context
     * @param order
     * @param query
     * @return
     */
    public static RequestParams getPeopleAddressRequestParams(final Context context,
                                                              final int order,
                                                              final String query,
                                                              final int page) {
        RequestParams requestParams = RequestParams.create();

        requestParams.putAll(new HashMap<String, Object>() {{
            putAll(generatePeopleAddressParams(context, order, query, page));
        }});

        return requestParams;
    }

    /**
     *
     * @param context
     * @param order
     * @param query
     * @return
     */
    public static Map<String, String> generatePeopleAddressParams(Context context,
                                                                  final int order,
                                                                  final String query,
                                                                  final int page) {
        return AuthUtil.generateParams(context, new HashMap<String, String>() {{
            put(PARAM_ORDER_BY, String.valueOf(order));
            put(PARAM_QUERY, query);
            put(PARAM_PAGE, String.valueOf(page));
        }});
    }

}
