package com.tokopedia.transaction.checkout.view;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.transaction.R;

/**
 * Created by Irfan Khoirul on 26/01/18.
 */

public class ShipmentDetailActivity extends BasePresenterActivity {

    private static final int DELAY_IN_MILISECOND = 500;
    private static final int REQUEST_CODE_SHIPMENT_CHOICE = 111;

    public static Intent createInstance(Activity activity) {
        return new Intent(activity, ShipmentDetailActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }

        openShipmentChoiceActivity();
    }

    private void openShipmentChoiceActivity() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivityForResult(ShipmentChoiceActivity.createInstance(
                        ShipmentDetailActivity.this), REQUEST_CODE_SHIPMENT_CHOICE);
                overridePendingTransition(R.anim.anim_bottom_up, 0);
            }
        }, DELAY_IN_MILISECOND);
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
        ShipmentDetailFragment fragment = ShipmentDetailFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.add(R.id.container, fragment, ShipmentChoiceFragment.class.getSimpleName());
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
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
