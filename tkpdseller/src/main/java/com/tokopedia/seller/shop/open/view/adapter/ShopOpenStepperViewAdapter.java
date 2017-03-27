package com.tokopedia.seller.shop.open.view.adapter;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.viewmodel.StepViewModel;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingInfoFragment;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingLocationFragment;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingLogisticFragment;

/**
 * Created by zulfikarrahman on 3/16/17.
 */

public class ShopOpenStepperViewAdapter extends AbstractNativeFragmentStepAdapter {

    private int districtId = 0;

    public ShopOpenStepperViewAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        switch (position){
            case 0:
                return new StepViewModel.Builder(context).setTitle("1").create();
            case 1:
                return new StepViewModel.Builder(context).setTitle("2").create();
            case 2:
                return new StepViewModel.Builder(context).setTitle("3").create();
        }
        return super.getViewModel(position);
    }

    @Override
    public Step createStep(@IntRange(from = 0L) int position) {
        switch (position){
            case 0:
                return ShopSettingInfoFragment.createInstance();
            case 1:
                return ShopSettingLocationFragment.getInstance();
            case 2:
                return ShopSettingLogisticFragment.getInstance(districtId);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
