package com.tokopedia.seller.shop.open.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenMandatoryInfoFragment;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenMandatoryLocationFragment;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenMandatoryLogisticFragment;
import com.tokopedia.seller.shop.open.view.model.ShopOpenStepperModel;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopOpenMandatoryActivity extends BaseStepperActivity<ShopOpenStepperModel>
        implements HasComponent<ShopSettingComponent>{

    public static final String EXTRA_LOGOUT_ON_BACK = "LOGOUT_ON_BACK";
    public static final String EXTRA_SHOP_NAME = "EXTRA_SHOP_NAME";
    public static final String EXTRA_SHOP_DOMAIN = "EXTRA_SHOP_DOMAIN";

    private ShopSettingComponent component;

    private List<Fragment> fragmentList;

    boolean isLogoutOnBack = false;

    public static Intent getIntent(Context context, boolean isLogoutOnBack, String shopName, String shopDomain) {
        Intent intent = new Intent(context, ShopOpenMandatoryActivity.class);
        intent.putExtra(EXTRA_LOGOUT_ON_BACK, isLogoutOnBack);
        intent.putExtra(EXTRA_SHOP_NAME, shopName);
        intent.putExtra(EXTRA_SHOP_DOMAIN, shopDomain);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_LOGOUT_ON_BACK)) {
            isLogoutOnBack = getIntent().getBooleanExtra(EXTRA_LOGOUT_ON_BACK, false);
        }
        if (intent.hasExtra(EXTRA_SHOP_NAME)) {
            stepperModel.setShopName(getIntent().getStringExtra(EXTRA_SHOP_NAME));
        }
        if (intent.hasExtra(EXTRA_SHOP_DOMAIN)) {
            stepperModel.setShopDomain(getIntent().getStringExtra(EXTRA_SHOP_DOMAIN));
        }
        initComponent();
    }

    @Override
    public ShopOpenStepperModel createNewStepperModel() {
        return new ShopOpenStepperModel();
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
        //TODO uncomment this, bypass for testing purpose
//        if (!SessionHandler.isMsisdnVerified()) {
//            Intent intent = SessionRouter.getPhoneVerificationActivationActivityIntent(this);
//            startActivity(intent);
//        }
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

    @Override
    public void onBackPressed() {
        if (getCurrentPosition() > 1) {
            super.onBackPressed();
        } else {
            if (isLogoutOnBack) {
                SessionHandler session = new SessionHandler(this);
                session.Logout(this);
            } else {
                super.onBackPressed();
            }
        }
    }

}