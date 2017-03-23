package com.tokopedia.seller.shop.open.view.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.model.LocationPass;
import com.tokopedia.seller.R;
import com.tokopedia.seller.app.BaseDiActivity;
import com.tokopedia.seller.shop.open.view.presenter.ShopOpenMandatoryPresenter;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingModule;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingLocationFragment;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingLocationListener;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLocationView;


/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopOpenMandatoryActivity
        extends BaseDiActivity<ShopOpenMandatoryPresenter, ShopSettingComponent>
        implements ShopSettingLocationListener {

    private static final int OPEN_MAP_CODE = 1000;
    //    StepperLayout stepperLayout;
    private FragmentManager fragmentManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(ShopSettingLocationFragment.TAG);
        if (fragment == null) {
            fragment = ShopSettingLocationFragment.getInstance();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment, ShopSettingLocationFragment.TAG);
            fragmentTransaction.commit();
        }


//        stepperLayout = (StepperLayout) findViewById(R.id.stepper_view);
//        stepperLayout.setAdapter(new ShopOpenStepperViewAdapter(getFragmentManager(), this));
    }

    @Override
    protected ShopSettingComponent initComponent() {
        return DaggerShopSettingComponent
                .builder()
                .shopSettingModule(new ShopSettingModule(this))
                .appComponent(getApplicationComponent())
                .build();
    }

    @Override
    protected ShopOpenMandatoryPresenter getPresenter() {
        return null;
    }

    @Override
    public void goToPickupLocationPicker(LocationPass locationPass) {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();

        int resultCode = availability.isGooglePlayServicesAvailable(this);

        if (ConnectionResult.SUCCESS == resultCode) {
            Intent intent = GeolocationActivity.createInstance(this, locationPass);
            startActivityForResult(intent, OPEN_MAP_CODE);
        } else {
            CommonUtils.dumper("Google play services unavailable");
            Dialog dialog = availability.getErrorDialog(this, resultCode, 0);
            dialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case OPEN_MAP_CODE:
                    LocationPass locationPass = data.getParcelableExtra(GeolocationActivity.EXTRA_EXISTING_LOCATION);
                    changeGoogleMapDataInLocationFragment(locationPass);
                    break;
            }
        }
    }

    private void changeGoogleMapDataInLocationFragment(LocationPass locationPass) {
        Fragment fragment = fragmentManager.findFragmentByTag(ShopSettingLocationFragment.TAG);
        if (fragment instanceof ShopSettingLocationView) {
            ((ShopSettingLocationView)fragment).changePickupLocation(locationPass);
        } else {
            throw new RuntimeException("Fragment must implement ShopSettingLocationView");
        }
    }

    @Override
    protected void initVar() {
    }

    @Override
    protected void setActionVar() {

    }

}
