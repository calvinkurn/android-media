package com.tokopedia.seller.shop.open.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.di.module.ShopOpenDomainModule;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenMandatoryInfoFragment;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenMandatoryLocationFragment;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenMandatoryLogisticFragment;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSettingComponent;
import com.tokopedia.seller.shop.open.di.component.DaggerShopOpenDomainComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopOpenMandatoryActivity extends BaseStepperActivity implements HasComponent<ShopOpenDomainComponent> {

    private List<Fragment> fragmentList;
    private ShopOpenDomainComponent component;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponent();
    }

    protected void initComponent() {
        if (component == null) {
            component = DaggerShopOpenDomainComponent
                    .builder()
                    .shopOpenDomainModule(new ShopOpenDomainModule())
                    .shopComponent(((SellerModuleRouter) getApplication()).getShopComponent())
                    .build();
        }
    }

    @NonNull
    @Override
    protected List<Fragment> getListFragment() {
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
//            fragmentList.add(new ShopOpenMandatoryInfoFragment());
            fragmentList.add(ShopOpenMandatoryLocationFragment.getInstance());
//            fragmentList.add(new ShopOpenMandatoryLogisticFragment());
            return fragmentList;
        } else {
            return fragmentList;
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
    public ShopOpenDomainComponent getComponent() {
        return component;
    }
}