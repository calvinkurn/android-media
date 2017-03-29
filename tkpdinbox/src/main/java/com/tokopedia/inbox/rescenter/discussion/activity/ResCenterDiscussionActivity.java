package com.tokopedia.inbox.rescenter.discussion.activity;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.discussion.fragment.ResCenterDiscussionFragment;

/**
 * Created by nisie on 3/29/17.
 */

public class ResCenterDiscussionActivity extends BasePresenterActivity {

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
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            Fragment fragment = ResCenterDiscussionFragment.createInstance();
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
