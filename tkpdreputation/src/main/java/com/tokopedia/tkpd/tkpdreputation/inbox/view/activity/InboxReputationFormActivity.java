package com.tokopedia.tkpd.tkpdreputation.inbox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationFormFragment;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationFormActivity extends BasePresenterActivity
        implements HasComponent {
    public static final String ARGS_REPUTATION_ID = "ARGS_REPUTATION_ID";

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
//        String id = getIntent().getExtras().getString(ARGS_REPUTATION_ID, "");
        String id = "";

        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (InboxReputationFormFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = InboxReputationFormFragment.createInstance(id);
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
    public Object getComponent() {
        return getApplicationComponent();
    }

    public static Intent getGiveReviewIntent(Context context) {
        Intent intent = new Intent(context, InboxReputationFormActivity.class);
        return intent;
    }
}
