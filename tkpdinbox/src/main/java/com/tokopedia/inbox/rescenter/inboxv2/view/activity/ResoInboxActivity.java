package com.tokopedia.inbox.rescenter.inboxv2.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.inboxv2.view.fragment.ResoInboxFragment;

/**
 * Created by yfsx on 24/01/18.
 */

public class ResoInboxActivity extends BasePresenterActivity implements HasComponent {
    public static final String TAG = ResoInboxFragment.class.getSimpleName();
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
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_res_chat;
    }

    @Override
    protected void initView() {
        Fragment fragment = ResoInboxFragment.getFragmentInstance(getIntent().getExtras());
        if (getSupportFragmentManager().findFragmentByTag(TAG) != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(com.tokopedia.core.R.id.container,
                            getSupportFragmentManager().findFragmentByTag(TAG))
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(com.tokopedia.core.R.id.container, fragment, TAG)
                    .commit();
        }
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    protected Fragment getNewFragment() {
        return ResoInboxFragment.getFragmentInstance(getIntent().getExtras());
    }

    @Override
    public Object getComponent() {
        return getApplicationComponent();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
