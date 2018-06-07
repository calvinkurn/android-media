package com.tokopedia.seller.product.imagepicker.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.tokopedia.imagepicker.picker.camera.ImagePickerCameraFragment;
import com.tokopedia.imagepicker.picker.gallery.ImagePickerGalleryFragment;
import com.tokopedia.imagepicker.picker.instagram.view.fragment.ImagePickerInstagramFragment;
import com.tokopedia.imagepicker.picker.main.adapter.ImagePickerViewPagerAdapter;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.imagepicker.util.CatalogConstant;
import com.tokopedia.seller.product.imagepicker.view.fragment.ImagePickerCatalogFragment;

/**
 * Created by zulfikarrahman on 6/6/18.
 */

public class ImagePickerViewPageAddProductAdapter extends ImagePickerViewPagerAdapter {
    private String catalogId;

    public ImagePickerViewPageAddProductAdapter(Context context, FragmentManager fm, ImagePickerBuilder imagePickerBuilder, String catalogId) {
        super(context, fm, imagePickerBuilder);
        this.catalogId = catalogId;
    }

    @Override
    public int getCount() {
        return CatalogConstant.COUNT_TAB_ADD_PRODUCT_IMAGE_PICKER;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.catalog);
            case 1:
                return context.getString(R.string.gallery);
            case 2:
                return context.getString(R.string.camera);
            case 3:
                return context.getString(R.string.instagram);
            default:
                return context.getString(R.string.gallery);
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ImagePickerCatalogFragment.createInstance(catalogId);
            case 1:
                return createGalleryFragment();
            case 2:
                return createCameraFragment();
            case 3:
                return createInstagramFragment();
            default:
                return new Fragment();
        }
    }
}
