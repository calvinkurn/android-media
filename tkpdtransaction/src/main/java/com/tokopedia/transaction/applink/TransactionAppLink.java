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
    public static final String ORDER_HISTORY = "tokopedia://orderlist/digital";
    public static final String ORDER_LIST_DIGITAL = "tokopedia://digital/order";
    public static final String ORDER_LIST_EVENTS = "tokopedia://events/order";
    public static final String ORDER_LIST_DEALS = "tokopedia://deals/order";
    public static final String ORDER_LIST_FLIGHTS = "tokopedia://pesawat/order";
    public static final String ORDER_DETAIL = "tokopedia://digital/order/{order_id}";
    public static final String ORDER_OMS_DETAIL = "tokopedia://order/{order_id}";

}
