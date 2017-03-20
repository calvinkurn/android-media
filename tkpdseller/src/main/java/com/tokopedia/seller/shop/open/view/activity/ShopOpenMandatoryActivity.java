package com.tokopedia.seller.shop.open.view.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.os.Bundle;
import android.view.View;

import com.stepstone.stepper.StepperLayout;
import com.tokopedia.seller.R;
import com.tokopedia.seller.app.BaseDiActivity;
import com.tokopedia.seller.shop.open.view.presenter.ShopOpenMandatoryPresenter;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingModule;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingLocationFragment;
import com.tokopedia.seller.shop.open.view.adapter.ShopOpenStepperViewAdapterOpenShop;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopOpenMandatoryActivity extends BaseDiActivity<ShopOpenMandatoryPresenter, ShopSettingComponent> {

    StepperLayout stepperLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_open_mandatory;
    }

    @Override
    protected void initView() {
        stepperLayout = (StepperLayout) findViewById(R.id.stepper_view);
        stepperLayout.setAdapter(new ShopOpenStepperViewAdapterOpenShop(getFragmentManager(), this));
    }

    @Override
    protected ShopSettingComponent initComponent() {
        return DaggerShopSettingComponent
                .builder()
                .shopSettingModule(new ShopSettingModule(this))
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

}
