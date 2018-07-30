package com.tokopedia.transaction.applink;

import com.tokopedia.abstraction.constant.TkpdAppLink;

/**
 * @author okasurya on 2/12/18.
 */

public class TransactionAppLink extends TkpdAppLink {
    public static final String PURCHASE_VERIFICATION = "tokopedia://buyer/payment";
    public static final String PURCHASE_ORDER = "tokopedia://buyer/order";
    public static final String PURCHASE_SHIPPING_CONFIRM = "tokopedia://buyer/shipping-confirm";
    public static final String PURCHASE_HISTORY = "tokopedia://buyer/history";
    public static final String ORDER_DETAIL = "tokopedia://digital/order/{order_id}";
    public static final String ORDER_OMS_DETAIL = "tokopedia://order/{order_id}";

    public static final String ORDER_LIST = "tokopedia://order";
}
