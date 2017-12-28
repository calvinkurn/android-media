package com.tokopedia.seller.shop.open.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.di.module.ShopOpenDomainModule;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenMandatoryLogisticFragment;
import com.tokopedia.seller.shop.open.view.model.ShopOpenStepperModel;
import com.tokopedia.seller.shop.setting.data.model.response.ResponseIsReserveDomain;
import com.tokopedia.seller.shop.open.di.component.DaggerShopOpenDomainComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopOpenMandatoryActivity extends BaseStepperActivity<ShopOpenStepperModel> implements HasComponent<ShopOpenDomainComponent> {

    public static final String EXTRA_RESPONSE_IS_RESERVE_DOMAIN = "EXTRA_RESPONSE_IS_RESERVE_DOMAIN";
    private List<Fragment> fragmentList;
    private ShopOpenDomainComponent component;

    public static Intent getIntent(Context context) {
        return new Intent(context, ShopOpenMandatoryActivity.class);
    }

    public static Intent getIntent(Context context, ResponseIsReserveDomain responseIsReserveDomain) {
        Intent intent = new Intent(context, ShopOpenMandatoryActivity.class);
        intent.putExtra(EXTRA_RESPONSE_IS_RESERVE_DOMAIN, responseIsReserveDomain);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
//            fragmentList.add(ShopSettingInfoFragment.createInstance());
//            fragmentList.add(ShopOpenMandatoryLocationFragment.getInstance());
            fragmentList.add(ShopOpenMandatoryLogisticFragment.newInstance());
            return fragmentList;
        } else {
            return fragmentList;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (!SessionHandler.isMsisdnVerified()) {
//            Intent intent = SessionRouter.getPhoneVerificationActivationActivityIntent(this);
//            startActivity(intent);
//        }
    }

    @Override
    public void finishPage() {
        Intent intent = ShopOpenCreateSuccessActivity.getIntent(this);
        this.startActivity(intent);
        super.finishPage();
    }

    @Override
    public ShopOpenDomainComponent getComponent() {
        initComponent();
        return component;
    }

}