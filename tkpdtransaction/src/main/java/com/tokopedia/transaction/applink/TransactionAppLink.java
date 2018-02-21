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
}
