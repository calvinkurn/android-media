package com.tokopedia.inbox.rescenter.shipping.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hangnadi on 12/14/16.
 */
public class NetworkParam {

    private static final int STATIC_GOLANG_VALUE = 2;
    private static final String PARAM_SERVER_LANGUAGE = "new_add";

    public static TKPDMapParam<String,String> getShippingListParams() {
        return new TKPDMapParam<>();
    }

    public static Map<String, String> generateHost() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_SERVER_LANGUAGE, String.valueOf(STATIC_GOLANG_VALUE));
        return params;
    }
}
