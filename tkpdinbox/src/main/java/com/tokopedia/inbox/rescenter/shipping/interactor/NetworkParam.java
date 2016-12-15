package com.tokopedia.inbox.rescenter.shipping.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

/**
 * Created by hangnadi on 12/14/16.
 */
public class NetworkParam {
    public static TKPDMapParam<String,String> getShippingListParams() {
        return new TKPDMapParam<>();
    }
}
