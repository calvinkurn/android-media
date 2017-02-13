package com.tokopedia.seller.gmsubscribe.view.checkout.widget;

/**
 * Created by sebastianuskh on 1/31/17.
 */
public interface CodeVoucherViewHolderCallback {
    void checkVoucher(String voucherCode);

    void dismissKeyboardFromVoucherEditText();
}
