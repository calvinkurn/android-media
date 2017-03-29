package com.tokopedia.seller.shop.open.view.adapter;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.viewmodel.StepViewModel;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingInfoFragment;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingLocationFragment;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingLogisticFragment;

/**
 * Created by zulfikarrahman on 3/16/17.
 */

public class ShopOpenStepperViewAdapter extends AbstractNativeFragmentStepAdapter {

    public static final int SHOP_SETTING_INFO_POSITION = 0;
    public static final int SHOP_SETTING_LOCATION_POSITION = 1;
    public static final int SHOP_SETTING_LOGICTIC_POSITION = 2;
    private final FragmentHolder fragmentHolder;

    public ShopOpenStepperViewAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
        fragmentHolder = new FragmentHolder();
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        switch (position){
            case 0:
                return new StepViewModel.Builder(context).setTitle(context.getString(R.string.open_shop_title_shop_information)).create();
            case 1:
                return new StepViewModel.Builder(context).setTitle(context.getString(R.string.open_shop_title_shop_location)).create();
            case 2:
                return new StepViewModel.Builder(context).setTitle(context.getString(R.string.open_shop_title_shop_shipping)).create();
        }
        return super.getViewModel(position);
    }

    @Override
    public Step createStep(@IntRange(from = 0L) int position) {
        switch (position){
            case SHOP_SETTING_INFO_POSITION:
                return fragmentHolder.getInfoFragment();
            case SHOP_SETTING_LOCATION_POSITION:
                return fragmentHolder.getLocationFragment();
            case SHOP_SETTING_LOGICTIC_POSITION:
                return fragmentHolder.getLogisticFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public Fragment getItemFragment(int position) {
        switch (position) {
            case SHOP_SETTING_INFO_POSITION:
                return fragmentHolder.getInfoFragment();
            case SHOP_SETTING_LOCATION_POSITION:
                return fragmentHolder.getLocationFragment();
            case SHOP_SETTING_LOGICTIC_POSITION:
                return fragmentHolder.getLogisticFragment();
            default:
                return null;
        }
    }

    private class FragmentHolder {
        private final ShopSettingInfoFragment infoFragment;
        private final ShopSettingLocationFragment locationFragment;
        private final ShopSettingLogisticFragment logisticFragment;

        public FragmentHolder() {
            infoFragment = ShopSettingInfoFragment.createInstance();
            locationFragment = ShopSettingLocationFragment.getInstance();
            logisticFragment = ShopSettingLogisticFragment.getInstance();
        }

        public ShopSettingInfoFragment getInfoFragment() {
            return infoFragment;
        }

        public ShopSettingLocationFragment getLocationFragment() {
            return locationFragment;
        }

        public ShopSettingLogisticFragment getLogisticFragment() {
            return logisticFragment;
        }
    }
}
