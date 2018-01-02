package com.tokopedia.tokocash.qrpayment.presentation.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.tokopedia.tokocash.R;

/**
 * Created by nabillasabbaha on 12/18/17.
 */

public class InfoSuccessPaymentQRView extends LinearLayout {

    public InfoSuccessPaymentQRView(Context context) {
        super(context);
        init();
    }

    public InfoSuccessPaymentQRView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InfoSuccessPaymentQRView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_tokocash_date_label, this, true);
    }
}
