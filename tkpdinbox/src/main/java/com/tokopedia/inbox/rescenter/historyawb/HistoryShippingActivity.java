package com.tokopedia.inbox.rescenter.historyawb;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.di.component.DaggerResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.detailv2.di.component.ResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.detailv2.di.module.ResolutionDetailModule;
import com.tokopedia.inbox.rescenter.historyawb.view.listener.HistoryShipping;
import com.tokopedia.inbox.rescenter.historyawb.view.listener.HistoryShippingViewListener;
import com.tokopedia.inbox.rescenter.historyawb.view.presenter.HistoryShippingImpl;

/**
 * Created by hangnadi on 3/23/17.
 */

public class HistoryShippingActivity extends BasePresenterActivity<HistoryShipping>
        implements HistoryShippingViewListener, HasComponent<ResolutionDetailComponent> {

    private static final String EXTRA_PARAM_RESOLUTION_ID = "resolution_id";
    private static final String EXTRA_PARAM_ALLOW_INPUT_NEW_SHIPPING_AWB = "is_allow_input_shipping_new_awb";
    private static final String TAG_HISTORY_SHIPPING_FRAGMENT =
            HistoryShippingFragment.class.getSimpleName();

    private Fragment fragment;
    private String resolutionID;
    private boolean allowInputNewShippingAwb;

    public static Intent newInstance(Context context, String resolutionID, boolean allowInputNewShippingAwb) {
        Intent intent = new Intent(context, HistoryShippingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM_RESOLUTION_ID, resolutionID);
        bundle.putBoolean(EXTRA_PARAM_ALLOW_INPUT_NEW_SHIPPING_AWB, allowInputNewShippingAwb);
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
    public void setAllowInputNewShippingAwb(boolean allowInputNewShippingAwb) {
        this.allowInputNewShippingAwb = allowInputNewShippingAwb;
    }

    @Override
    public boolean isAllowInputNewShippingAwb() {
        return allowInputNewShippingAwb;
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
        setAllowInputNewShippingAwb(extras.getBoolean(EXTRA_PARAM_ALLOW_INPUT_NEW_SHIPPING_AWB));
    }

    @Override
    protected void initialPresenter() {
        presenter = new HistoryShippingImpl(this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
