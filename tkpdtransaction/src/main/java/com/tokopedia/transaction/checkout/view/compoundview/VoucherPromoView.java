package com.tokopedia.transaction.checkout.view.compoundview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.tokopedia.design.voucher.VoucherCartHachikoView;
import com.tokopedia.transaction.R;

/**
 * @author anggaprasetiyo on 08/03/18.
 */

public class VoucherPromoView extends VoucherCartHachikoView {
    public VoucherPromoView(@NonNull Context context) {
        super(context);
    }

    public VoucherPromoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VoucherPromoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_holder_voucher_promo_new_cart;
    }
}
