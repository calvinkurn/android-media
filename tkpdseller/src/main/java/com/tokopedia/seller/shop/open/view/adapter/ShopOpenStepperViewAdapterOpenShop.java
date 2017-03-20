package com.tokopedia.seller.shop.open.view.adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenStepOneFragment;

/**
 * Created by zulfikarrahman on 3/16/17.
 */

public class ShopOpenStepperViewAdapterOpenShop extends AbstractNativeFragmentStepAdapter {

    public ShopOpenStepperViewAdapterOpenShop(@NonNull FragmentManager fm, @NonNull Context context) {
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
                return ShopOpenStepOneFragment.createInstance();
            case 1:
                return ShopOpenStepOneFragment.createInstance();
            case 2:
                return ShopOpenStepOneFragment.createInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
