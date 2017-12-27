package com.tokopedia.seller.shop.open.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.di.module.ShopOpenDomainModule;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenMandatoryInfoFragment;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenMandatoryLocationFragment;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenMandatoryLogisticFragment;
import com.tokopedia.seller.shop.open.view.model.ShopOpenStepperModel;
import com.tokopedia.seller.shop.setting.data.model.response.ResponseIsReserveDomain;
import com.tokopedia.seller.shop.setting.data.model.response.UserData;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSettingComponent;
import com.tokopedia.seller.shop.open.di.component.DaggerShopOpenDomainComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopOpenMandatoryActivity extends BaseStepperActivity<ShopOpenStepperModel> implements HasComponent<ShopOpenDomainComponent>{

    public static final String EXTRA_RESPONSE_IS_RESERVE_DOMAIN = "EXTRA_RESPONSE_IS_RESERVE_DOMAIN";
    private List<Fragment> fragmentList;
    private ShopOpenDomainComponent component;

    boolean isLogoutOnBack = false;

    public static Intent getIntent(Context context) {
        return new Intent(context, ShopOpenMandatoryActivity.class);
    }

    public static Intent getIntent(Context context, ResponseIsReserveDomain responseIsReserveDomain) {
        Intent intent = new Intent(context, ShopOpenMandatoryActivity.class);
        intent.putExtra(EXTRA_RESPONSE_IS_RESERVE_DOMAIN, responseIsReserveDomain);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_RESPONSE_IS_RESERVE_DOMAIN)) {
            stepperModel.setResponseIsReserveDomain((ResponseIsReserveDomain) getIntent().getParcelableExtra(EXTRA_RESPONSE_IS_RESERVE_DOMAIN));
        }
        initComponent();
    }

    @Override
    public ShopOpenStepperModel createNewStepperModel() {
        return new ShopOpenStepperModel();
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
            fragmentList.add(new ShopOpenMandatoryInfoFragment());
            fragmentList.add(ShopOpenMandatoryLocationFragment.getInstance());
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
    public ShopOpenDomainComponent getComponent() {
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