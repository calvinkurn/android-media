package com.tokopedia.transaction.checkout.view.view.shippingoptions;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentDetailData;

/**
 * Created by Irfan Khoirul on 26/01/18.
 */

public class ShipmentDetailActivity extends BasePresenterActivity
        implements ShipmentDetailFragment.FragmentListener {

    public static final String EXTRA_SHIPMENT_DETAIL_DATA = "shipmentDetailData";
    public static final String EXTRA_POSITION = "position";

    public static Intent createInstance(Activity activity, ShipmentDetailData shipmentDetailData,
                                        int position) {
        Intent intent = new Intent(activity, ShipmentDetailActivity.class);
        intent.putExtra(EXTRA_SHIPMENT_DETAIL_DATA, shipmentDetailData);
        intent.putExtra(EXTRA_POSITION, position);
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
        ShipmentDetailFragment fragment = ShipmentDetailFragment.newInstance(
                (ShipmentDetailData) getIntent().getParcelableExtra(EXTRA_SHIPMENT_DETAIL_DATA));
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.add(R.id.container, fragment, ShipmentDetailFragment.class.getSimpleName());
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

    @Override
    public void onCourierSelected(ShipmentDetailData shipmentDetailData) {
        Intent intentResult = new Intent();
        intentResult.putExtra(EXTRA_SHIPMENT_DETAIL_DATA, shipmentDetailData);
        intentResult.putExtra(EXTRA_POSITION, getIntent().getIntExtra(EXTRA_POSITION, 0));
        setResult(Activity.RESULT_OK, intentResult);
        finish();
    }
}
