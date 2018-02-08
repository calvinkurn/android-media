package com.tokopedia.inbox.rescenter.detail.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.inbox.rescenter.detail.fragment.DetailResCenterFragment;
import com.tokopedia.inbox.rescenter.detail.listener.ResCenterView;
import com.tokopedia.inbox.rescenter.detail.model.passdata.ActivityParamenterPassData;
import com.tokopedia.inbox.rescenter.detail.presenter.ResCenterImpl;
import com.tokopedia.inbox.rescenter.detail.presenter.ResCenterPresenter;
import com.tokopedia.inbox.rescenter.detail.service.DetailResCenterReceiver;

/**
 * Created by hangnadi on 2/9/16.
 */

@Deprecated
public class ResCenterActivity extends BasePresenterActivity<ResCenterPresenter> implements
        ResCenterView,
        DetailResCenterReceiver.Receiver {

    public static final String EXTRA_RES_CENTER_PASS = "EXTRA_RES_CENTER_PASS";

    private Uri uriData;
    private Bundle bundleData;
    private DetailResCenterReceiver mReceiver;

    public static Intent newInstance(Context context, @NonNull ActivityParamenterPassData activityParamenterPassData) {
        Intent intent = new Intent(context, ResCenterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_RES_CENTER_PASS, activityParamenterPassData);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_INBOX_RESOLUTION_CENTER_DETAIL;
    }

    @Override
    protected void setupURIPass(Uri data) {
        this.uriData = data;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.bundleData = extras;
    }

    @Override
    protected void initialPresenter() {
        presenter = new ResCenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_res_center_detail;
    }

    @Override
    protected void initView() {}

    @Override
    protected void setViewListener() {
        presenter.initFragment(this, uriData, bundleData);
    }

    @Override
    protected void initVar() {
        mReceiver = new DetailResCenterReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    @Override
    protected void setActionVar() {}

    @Override
    public void inflateFragment(Fragment fragment, String tag) {
        if (getFragmentManager().findFragmentByTag(tag) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, tag)
                    .commit();
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        DetailResCenterFragment fragment = (DetailResCenterFragment)
                getFragmentManager().findFragmentByTag(DetailResCenterFragment.class.getSimpleName());
        presenter.onReceiveResult(resultCode, resultData, fragment);
    }

    @Override
    public void replyConversation(String resolutionID) {
        presenter.replyConversation(this, resolutionID, mReceiver);
    }

    @Override
    public void actionFinishReturSolution(String resolutionID) {
        presenter.actionFinishReturSolution(this, resolutionID, mReceiver);
    }

    @Override
    public void actionAcceptSolution(String resolutionID) {
        presenter.actionAcceptSolution(this, resolutionID, mReceiver);
    }

    @Override
    public void actionAcceptAdminSolution(String resolutionID) {
        presenter.actionAcceptAdminSolution(this, resolutionID, mReceiver);
    }

    @Override
    public void actionCancelResolution(String resolutionID) {
        presenter.actionCancelResolution(this, resolutionID, mReceiver);
    }

    @Override
    public void actionReportResolution(String resolutionID) {
        presenter.actionReportResolution(this, resolutionID, mReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getFragmentManager().findFragmentByTag(DetailResCenterFragment.class.getSimpleName());
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot() && GlobalConfig.isSellerApp()) {
            startActivity(SellerAppRouter.getSellerHomeActivity(this));
            finish();
        } else if (isTaskRoot()){
            startActivity(HomeRouter.getHomeActivity(this));
            finish();
        }
        super.onBackPressed();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
