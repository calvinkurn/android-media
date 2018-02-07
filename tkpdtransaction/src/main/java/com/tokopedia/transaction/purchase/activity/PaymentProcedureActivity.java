package com.tokopedia.transaction.purchase.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.activity.BaseWebViewActivity;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.transaction.purchase.fragment.PaymentProcedureWebViewFragment;

/**
 * @author okasurya on 2/6/18.
 */

public class PaymentProcedureActivity extends BaseWebViewActivity {
    private PaymentProcedureWebViewFragment fragment;

    public static Intent newIntent(Context context, String howtopayUrl) {
        Bundle bundle = new Bundle();
        bundle.putString(PaymentProcedureWebViewFragment.EXTRA_URL, howtopayUrl);
        return new Intent(context, PaymentProcedureActivity.class).putExtras(bundle);
    }

    @Override
    protected Fragment getNewFragment() {
        fragment = PaymentProcedureWebViewFragment.createInstance(getIntent().getExtras());
        return fragment;
    }

    @Override
    protected Intent getHelpIntent() {
        return InboxRouter.getContactUsActivityIntent(this);
    }

    @Override
    public void onBackPressed() {
        try {
            if (fragment.getWebview().canGoBack()) {
                fragment.getWebview().goBack();
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            super.onBackPressed();
        }
    }
}
