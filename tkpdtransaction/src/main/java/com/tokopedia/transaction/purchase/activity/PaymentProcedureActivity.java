package com.tokopedia.transaction.purchase.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.transaction.purchase.fragment.PaymentProcedureWebViewFragment;

/**
 * @author okasurya on 2/6/18.
 */

public class PaymentProcedureActivity extends BaseSimpleActivity {

    public static Intent newIntent(Context context, String howtopayUrl) {
        Bundle bundle = new Bundle();
        bundle.putString(URL_EXTRA, howtopayUrl);
        return new Intent(context, PaymentProcedureActivity.class).putExtras(bundle);
    }

    @Override
    protected Fragment getNewFragment() {
        return PaymentProcedureWebViewFragment.newInstance(getIntent().getExtras());
    }
}
