package com.tokopedia.seller.gmsubscribe.view.widget.checkout;

import android.content.Context;

/**
 * Created by sebastianuskh on 1/31/17.
 */
public interface CodeVoucherViewHolderCallback {
    void checkVoucher(String voucherCode);

    Context getActivity();
}
