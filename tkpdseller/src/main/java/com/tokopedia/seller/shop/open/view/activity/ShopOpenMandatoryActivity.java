package com.tokopedia.seller.shop.open.view.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;

import com.stepstone.stepper.StepperLayout;
import com.tokopedia.core.gallery.ImageGalleryEntry;
import com.tokopedia.seller.R;
import com.tokopedia.seller.app.BaseDiActivity;
import com.tokopedia.seller.myproduct.fragment.AddProductFragment;
import com.tokopedia.seller.shop.open.view.adapter.ShopOpenStepperViewAdapter;
import com.tokopedia.seller.shop.open.view.presenter.ShopOpenMandatoryPresenter;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingModule;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingLocationFragment;
import com.tokopedia.seller.shop.setting.view.listener.ListenerShopSettingInfo;
import com.tokopedia.seller.shop.utils.UploadPhotoShopTask;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopOpenMandatoryActivity extends BaseDiActivity<ShopOpenMandatoryPresenter, ShopSettingComponent>
        implements ListenerShopSettingInfo {

    public static final int MAX_SELECTION_PICK_IMAGE = 1;

    StepperLayout stepperLayout;
    private FragmentManager fragmentManager;
    private ListenerShopSettingInfo.ListenerOnImagePickerReady listenerOnImagePickerReady;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_open_mandatory;
    }

    @Override
    protected void initView() {
//        fragmentManager = getFragmentManager();
//        Fragment fragment = fragmentManager.findFragmentByTag(ShopSettingLocationFragment.TAG);
//        if (fragment == null) {
//            fragment = ShopSettingLocationFragment.getInstance();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.container, fragment, ShopSettingLocationFragment.TAG);
//            fragmentTransaction.commit();
//        }

        stepperLayout = (StepperLayout) findViewById(R.id.stepper_view);
        stepperLayout.setAdapter(new ShopOpenStepperViewAdapter(getFragmentManager(), this));
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
    protected void initVar() {
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageGalleryEntry.onActivityForResult(new ImageGalleryEntry.GalleryListener() {
            @Override
            public void onSuccess(ArrayList<String> imageUrls) {
                File file = UploadPhotoShopTask.writeImageToTkpdPath(AddProductFragment.compressImage(imageUrls.get(0)));
                if (listenerOnImagePickerReady != null) {
                    listenerOnImagePickerReady.onImageReady(file.getPath());
                }
            }

            @Override
            public void onSuccess(String path, int position) {
                File file = UploadPhotoShopTask.writeImageToTkpdPath(AddProductFragment.compressImage(path));
                if (listenerOnImagePickerReady != null) {
                    listenerOnImagePickerReady.onImageReady(file.getPath());
                }
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

    @Override
    public void onBrowseImageAction(ListenerOnImagePickerReady listenerOnImagePickerReady) {
        this.listenerOnImagePickerReady = listenerOnImagePickerReady;
        ImageGalleryEntry.moveToImageGallery(this, 0, MAX_SELECTION_PICK_IMAGE);
    }
}
