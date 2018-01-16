package com.tokopedia.seller.shop.open.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.router.OldSessionRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.ResponseIsReserveDomain;
import com.tokopedia.seller.shop.open.di.component.DaggerShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.di.module.ShopOpenDomainModule;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenMandatoryInfoFragment;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenMandatoryLocationFragment;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenMandatoryLogisticFragment;
import com.tokopedia.seller.shop.open.view.model.ShopOpenStepperModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopOpenMandatoryActivity extends BaseStepperActivity<ShopOpenStepperModel>
        implements HasComponent<ShopOpenDomainComponent>,
        ShopOpenMandatoryLogisticFragment.OnShopOpenLogisticFragmentListener{

    public static final String EXTRA_RESPONSE_IS_RESERVE_DOMAIN = "EXTRA_RESPONSE_IS_RESERVE_DOMAIN";
    public static final int REQUEST_PHONE_VERIFICATION = 300;
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
            fragmentList.add(ShopOpenMandatoryInfoFragment.createInstance());
            fragmentList.add(ShopOpenMandatoryLocationFragment.getInstance());
            fragmentList.add(ShopOpenMandatoryLogisticFragment.newInstance());
            return fragmentList;
        } else {
            return fragmentList;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SessionHandler.isMsisdnVerified()) {
            Intent intent = OldSessionRouter.getPhoneVerificationActivationActivityIntent(this);
            startActivityForResult(intent, REQUEST_PHONE_VERIFICATION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PHONE_VERIFICATION) {
            if (resultCode != Activity.RESULT_OK) {
                this.finish();
            }
        }
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

    @Override
    public void goToPickupLocation() {
        onBackPressed();
    }

}