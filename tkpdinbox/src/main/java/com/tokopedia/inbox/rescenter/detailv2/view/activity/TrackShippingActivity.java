package com.tokopedia.inbox.rescenter.detailv2.view.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.TrackShippingActivityListener;
import com.tokopedia.inbox.rescenter.detailv2.view.presenter.TrackShippingActivityPresenter;

public class TrackShippingActivity extends BasePresenterActivity<TrackShippingActivityPresenter>
        implements TrackShippingActivityListener.View, HasComponent {

    public static final String PARAM_SHIPMENT_ID = "shipment_id";
    public static final String PARAM_SHIPPING_REF = "shipping_ref";

    String shipmentId;
    String shippingRef;

    public static Intent newInstance(Context context, String shipmentId, String shippingRef) {
        Intent intent = new Intent(context, TrackShippingActivity.class);
        intent.putExtra(PARAM_SHIPMENT_ID, shipmentId);
        intent.putExtra(PARAM_SHIPPING_REF, shippingRef);
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
    protected void initVar() {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        shipmentId = extras.getString(PARAM_SHIPMENT_ID);
        shippingRef = extras.getString(PARAM_SHIPPING_REF);
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialPresenter() {
        presenter = new TrackShippingActivityPresenter(this, shipmentId, shippingRef);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_track_shipping;
    }

    @Override
    protected void initView() {
        presenter.initFragment();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_x_black);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getBottomBackSheetActivityTransition();
    }

    public void getBottomBackSheetActivityTransition() {
        overridePendingTransition(R.anim.push_down, R.anim.pull_up);
    }
}
