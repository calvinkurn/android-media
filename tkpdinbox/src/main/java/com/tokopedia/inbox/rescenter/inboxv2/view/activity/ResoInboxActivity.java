package com.tokopedia.inbox.rescenter.inboxv2.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.inbox.rescenter.inboxv2.view.di.DaggerResoInboxComponent;
import com.tokopedia.inbox.rescenter.inboxv2.view.di.ResoInboxComponent;
import com.tokopedia.inbox.rescenter.inboxv2.view.fragment.ResoInboxFragment;

/**
 * Created by yfsx on 24/01/18.
 */

public class ResoInboxActivity extends BaseSimpleActivity implements HasComponent<ResoInboxComponent> {

    public static final String PARAM_IS_SELLER = "is_seller";
    public static final String PARAM_HEADER_TEXT = "header_text";

    public static Intent newSellerInstance(Context context) {
        Intent intent = new Intent(context, ResoInboxActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(PARAM_IS_SELLER, true);
        bundle.putString(PARAM_HEADER_TEXT, "Komplain Sebagai Penjual");
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newBuyerInstance(Context context) {
        Intent intent = new Intent(context, ResoInboxActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(PARAM_IS_SELLER, false);
        bundle.putString(PARAM_HEADER_TEXT, "Komplain Sebagai Pembeli");
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setTitle(getIntent().getStringExtra(PARAM_HEADER_TEXT));
    }

    @Override
    protected Fragment getNewFragment() {
        return ResoInboxFragment.getFragmentInstance(getIntent().getExtras());
    }

    @Override
    public ResoInboxComponent getComponent() {
        return DaggerResoInboxComponent.builder().build();
    }
}
