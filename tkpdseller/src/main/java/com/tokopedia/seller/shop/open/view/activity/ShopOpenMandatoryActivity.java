package com.tokopedia.seller.shop.open.view.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.gallery.ImageGalleryEntry;
import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingModule;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingLocationListener;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingLogisticListener;
import com.tokopedia.seller.shop.setting.view.listener.ListenerShopSettingInfo;
import com.tokopedia.seller.shop.setting.view.model.ShopSettingLocationModel;
import com.tokopedia.seller.shop.utils.UploadPhotoShopTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopOpenMandatoryActivity extends BaseStepperActivity implements
        ShopSettingLocationListener,
        HasComponent<ShopSettingComponent>,
        ListenerShopSettingInfo, ShopSettingLogisticListener {

    //    StepperLayout stepperLayout;
    public static final int MAX_SELECTION_PICK_IMAGE = 1;
    private static final int OPEN_MAP_CODE = 1000;
    private ShopSettingComponent component;
    private ListenerShopSettingInfo.ListenerOnImagePickerReady listenerOnImagePickerReady;

    private List<Fragment> fragmentList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponent();

    }

    @NonNull
    @Override
    protected List<Fragment> getListFragment() {
        return fragmentList;
    }

    protected void initComponent() {
        component = DaggerShopSettingComponent
                .builder()
                .shopSettingModule(new ShopSettingModule())
                .shopComponent(((SellerModuleRouter) getApplication()).getShopComponent())
                .build();
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
    public void goToShopSettingLogisticFragment(ShopSettingLocationModel model) {
        updateFragmentLogistic(model.getDistrictCode());
//        stepperLayout.setCurrentStepPosition(2);
    }

    private void updateFragmentLogistic(int districtCode) {
//        Fragment fragment = stepAdapter.getItemFragment(ShopOpenStepperViewAdapter.SHOP_SETTING_LOGICTIC_POSITION);
//        if (fragment instanceof ShopSettingLogisticView) {
//            ((ShopSettingLogisticView) fragment).changeDistrictCode(districtCode);
//        } else {
//            throw new RuntimeException("Fragment must implement ShopSettingLogisticView");
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case OPEN_MAP_CODE:
                    LocationPass locationPass =
                            data.getParcelableExtra(GeolocationActivity.EXTRA_EXISTING_LOCATION);
                    changeGoogleMapDataInLocationFragment(locationPass);
                    break;
            }
        }
        ImageGalleryEntry.onActivityForResult(new ImageGalleryEntry.GalleryListener() {
            @Override
            public void onSuccess(ArrayList<String> imageUrls) {
//                File file = UploadPhotoShopTask.writeImageToTkpdPath(AddProductFragment.compressImage(imageUrls.get(0)));
//                if (listenerOnImagePickerReady != null) {
//                    listenerOnImagePickerReady.onImageReady(file.getPath());
//                }
            }

            @Override
            public void onSuccess(String path) {

            }

            public void onSuccess(String path, int position) {
//                File file = UploadPhotoShopTask.writeImageToTkpdPath(AddProductFragment.compressImage(path));
//                if (listenerOnImagePickerReady != null) {
//                    listenerOnImagePickerReady.onImageReady(file.getPath());
//                }
            }

            @Override
            public void onFailed(String message) {

            }

            @Override
            public Context getContext() {
                return ShopOpenMandatoryActivity.this;
            }
        }, requestCode, resultCode, data);
    }

    private void changeGoogleMapDataInLocationFragment(LocationPass locationPass) {
//        Fragment fragment =
//                stepAdapter.getItemFragment(
//                        ShopOpenStepperViewAdapter.SHOP_SETTING_LOCATION_POSITION
//                );
//        if (fragment instanceof ShopSettingLocationView) {
//            ((ShopSettingLocationView)fragment).changePickupLocation(locationPass);
//        } else {
//            throw new RuntimeException("Fragment must implement ShopSettingLocationView");
//        }
    }


    @Override
    public ShopSettingComponent getComponent() {
        return component;
    }

    @Override
    public void onBrowseImageAction(ListenerOnImagePickerReady listenerOnImagePickerReady) {

    }

    @Override
    public void goBackToLocation() {
//        stepperLayout.setCurrentStepPosition(
//                ShopOpenStepperViewAdapter.SHOP_SETTING_LOCATION_POSITION
//        );
    }
}
