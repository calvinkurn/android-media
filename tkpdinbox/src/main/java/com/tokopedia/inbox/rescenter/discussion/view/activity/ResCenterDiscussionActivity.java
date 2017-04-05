package com.tokopedia.inbox.rescenter.discussion.view.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.discussion.view.fragment.ResCenterDiscussionFragment;

/**
 * Created by nisie on 3/29/17.
 */

public class ResCenterDiscussionActivity extends BasePresenterActivity {

    private static final String PARAM_RESOLUTION_ID = "PARAM_RESOLUTION_ID";
    private static final String PARAM_FLAG_RECEIVED = "PARAM_FLAG_RECEIVED";


    public static Intent createIntent(Context context, String resolutionId, boolean flagReceived) {
        Intent intent = new Intent(context, ResCenterDiscussionActivity.class);
        intent.putExtra(PARAM_RESOLUTION_ID, resolutionId);
        intent.putExtra(PARAM_FLAG_RECEIVED, flagReceived);

        return intent;
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
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        String resolutionId = "";
        boolean flagReceived = false;

        if (getIntent().getExtras() != null) {
            resolutionId = getIntent().getExtras().getString(PARAM_RESOLUTION_ID);
            flagReceived = getIntent().getExtras().getBoolean(PARAM_FLAG_RECEIVED);

        }

        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            Fragment fragment = ResCenterDiscussionFragment.createInstance(resolutionId,flagReceived);
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, fragment.getClass().getSimpleName())
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
}
