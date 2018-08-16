package com.tokopedia.transaction.applink;

import com.tokopedia.abstraction.constant.TkpdAppLink;
import com.tokopedia.core.gcm.Constants;

/**
 * @author okasurya on 2/12/18.
 */

public class TransactionAppLink extends TkpdAppLink {
    public static final String ORDER_HISTORY = "tokopedia://orderlist/digital";
    public static final String ORDER_DETAIL = "tokopedia://digital/order/{order_id}";
    public static final String ORDER_OMS_DETAIL = "tokopedia://order/{order_id}";
}
