package com.tokopedia.inbox.rescenter.detailv2.view.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.NextActionActivityListener;
import com.tokopedia.inbox.rescenter.detailv2.view.presenter.NextActionActivityPresenter;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;

/**
 * Created by yoasfs on 16/10/17.
 */

public class NextActionActivity
        extends BasePresenterActivity<NextActionActivityListener.Presenter>
        implements NextActionActivityListener.View, HasComponent {

    public static final String PARAM_RESOLUTION_ID = "resolution_id";
    public static final String PARAM_NEXT_ACTION = "next_action";
    public static final String PARAM_RESOLUTION_STATUS = "reso_status";

    NextActionDomain nextActionDomain;
    String resolutionId;
    int resolutionStatus;

    public static Intent newInstance(Context context, String resolutionId, NextActionDomain nextActionDomain, int resolutionStatus) {
        Intent intent = new Intent(context, NextActionActivity.class);
        intent.putExtra(PARAM_RESOLUTION_ID, resolutionId);
        intent.putExtra(PARAM_NEXT_ACTION, nextActionDomain);
        intent.putExtra(PARAM_RESOLUTION_STATUS, resolutionStatus);
        return intent;
    }

    @Override
    public Object getComponent() {
        return getApplicationComponent();
    }

    @Override
    public void inflateFragment(Fragment fragment, String TAG) {
        if (getFragmentManager().findFragmentByTag(TAG) != null) {
            getFragmentManager().beginTransaction()
                    .replace(com.tokopedia.core2.R.id.container,
                            getFragmentManager().findFragmentByTag(TAG))
                    .commit();
        } else {
            getFragmentManager().beginTransaction()
                    .add(com.tokopedia.core2.R.id.container, fragment, TAG)
                    .commit();
        }
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        resolutionId = extras.getString(PARAM_RESOLUTION_ID);
        nextActionDomain = extras.getParcelable(PARAM_NEXT_ACTION);
        resolutionStatus = extras.getInt(PARAM_RESOLUTION_STATUS);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void initialPresenter() {
        presenter = new NextActionActivityPresenter(this, resolutionId, nextActionDomain, resolutionStatus);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_next_action;
    }

    @Override
    protected void initView() {
        toolbar.setTitle(getResources().getString(R.string.string_title_next_step));
        presenter.initFragment();

    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_x_black);
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

    public void getBottomBackSheetActivityTransition() {
        overridePendingTransition(R.anim.push_down, R.anim.pull_up);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getBottomBackSheetActivityTransition();
    }
}
