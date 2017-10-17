package com.tokopedia.inbox.rescenter.historyaction;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.di.component.DaggerResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.detailv2.di.component.ResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.detailv2.di.module.ResolutionDetailModule;
import com.tokopedia.inbox.rescenter.historyaction.view.listener.HistoryAction;
import com.tokopedia.inbox.rescenter.historyaction.view.listener.HistoryActionViewListener;
import com.tokopedia.inbox.rescenter.historyaction.view.presenter.HistoryActionImpl;

/**
 * Created by hangnadi on 3/23/17.
 */

public class HistoryActionActivity extends BasePresenterActivity<HistoryAction>
        implements HistoryActionViewListener, HasComponent<ResolutionDetailComponent> {

    private static final String EXTRA_PARAM_RESOLUTION_ID = "resolution_id";
    private static final String TAG_HISTORY_SHIPPING_FRAGMENT =
            HistoryActionFragment.class.getSimpleName();

    private Fragment fragment;
    private String resolutionID;

    public static Intent newInstance(Context context, String resolutionID) {
        Intent intent = new Intent(context, HistoryActionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM_RESOLUTION_ID, resolutionID);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public String getResolutionID() {
        return resolutionID;
    }

    @Override
    public void setResolutionID(String resolutionID) {
        this.resolutionID = resolutionID;
    }

    @Override
    public void setHistoryShippingFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        setResolutionID(extras.getString(EXTRA_PARAM_RESOLUTION_ID));
    }

    @Override
    protected void initialPresenter() {
        presenter = new HistoryActionImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_res_center_detail;
    }

    @Override
    protected void initView() {
        presenter.generateFragment();
        inflateFragment();
    }

    @Override
    public void inflateFragment() {
        if (getFragmentManager().findFragmentByTag(TAG_HISTORY_SHIPPING_FRAGMENT) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, getFragment(), TAG_HISTORY_SHIPPING_FRAGMENT)
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

    @Override
    public ResolutionDetailComponent getComponent() {
        return DaggerResolutionDetailComponent.builder()
                .appComponent(getApplicationComponent())
                .resolutionDetailModule(new ResolutionDetailModule())
                .build();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
