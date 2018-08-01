package com.tokopedia.transaction.applink;

import com.tokopedia.abstraction.constant.TkpdAppLink;

/**
 * @author okasurya on 2/12/18.
 */

public class TransactionAppLink extends TkpdAppLink {
    // TODO: 8/1/18 convert to PMS
    public static final String PURCHASE_VERIFICATION = "tokopedia://buyer/payment";
    public static final String ORDER_DETAIL = "tokopedia://digital/order/{order_id}";
    public static final String ORDER_OMS_DETAIL = "tokopedia://order/{order_id}";

    public static final String ORDER_LIST = "tokopedia://order";
}
