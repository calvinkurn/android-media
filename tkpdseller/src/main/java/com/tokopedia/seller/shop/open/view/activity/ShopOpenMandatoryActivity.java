package com.tokopedia.seller.shop.open.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenMandatoryInfoFragment;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenMandatoryLocationFragment;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenMandatoryLogisticFragment;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopOpenMandatoryActivity extends BaseStepperActivity implements HasComponent<ShopSettingComponent> {

    private ShopSettingComponent component;

    private List<Fragment> fragmentList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponent();
    }

    protected void initComponent() {
        component = DaggerShopSettingComponent
                .builder()
                .shopSettingModule(new ShopSettingModule())
                .shopComponent(((SellerModuleRouter) getApplication()).getShopComponent())
                .build();
    }

    @NonNull
    @Override
    protected List<Fragment> getListFragment() {
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
            fragmentList.add(new ShopOpenMandatoryInfoFragment());
            fragmentList.add(new ShopOpenMandatoryLocationFragment());
            fragmentList.add(new ShopOpenMandatoryLogisticFragment());
            return fragmentList;
        } else {
            return fragmentList;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SessionHandler.isMsisdnVerified()) {
            Intent intent = SessionRouter.getPhoneVerificationActivationActivityIntent(this);
            startActivity(intent);
        }
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
    public ShopSettingComponent getComponent() {
        return component;
    }
}