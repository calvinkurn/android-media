package com.tokopedia.tkpd.tkpdreputation.inbox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationDetailFragment;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailActivity extends BasePresenterActivity implements HasComponent {

    public static final String ARGS_REPUTATION_ID = "ARGS_REPUTATION_ID";
    public static final String ARGS_POSITION = "ARGS_POSITION";
    public static final String ARGS_TAB = "ARGS_TAB";

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
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        String id = getIntent().getExtras().getString(ARGS_REPUTATION_ID);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(InboxReputationDetailFragment
                .class.getSimpleName());
        if (fragment == null) {
            fragment = InboxReputationDetailFragment.createInstance(id);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,
                fragment,
                fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

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

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    public static Intent getCallingIntent(Context context, String reputationId,
                                          int adapterPosition,
                                          int tab) {
        Intent intent = new Intent(context, InboxReputationDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(InboxReputationDetailActivity.ARGS_REPUTATION_ID, reputationId);
        bundle.putInt(InboxReputationDetailActivity.ARGS_POSITION, adapterPosition);
        bundle.putInt(InboxReputationDetailActivity.ARGS_TAB, tab);
        intent.putExtras(bundle);
        return intent;
    }
}
