package com.tokopedia.seller.shop.open.view.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.shop.common.di.component.ShopComponent;
import com.tokopedia.seller.shop.open.di.component.DaggerShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.di.module.ShopOpenDomainModule;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenDomainFragment;
import com.tokopedia.seller.shop.open.view.listener.ShopCheckDomainView;
import com.tokopedia.seller.shop.open.view.presenter.ShopCheckIsReservePresenterImpl;
import com.tokopedia.seller.shop.open.view.presenter.ShopOpenDomainPresenter;
import com.tokopedia.seller.shop.open.view.presenter.ShopOpenDomainPresenterImpl;

import javax.inject.Inject;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopOpenDomainActivity extends BaseSimpleActivity
        implements HasComponent<ShopOpenDomainComponent>,ShopCheckDomainView {
    boolean isLogoutOnBack = false;

    public static final String EXTRA_LOGOUT_ON_BACK = "LOGOUT_ON_BACK";

    private ShopOpenDomainComponent shopOpenDomainComponent;

    @Inject
    ShopCheckIsReservePresenterImpl shopCheckIsReservePresenter;
    private boolean isReservingDomain;

    public static Intent getIntent(Context context, boolean isLogoutOnBack){
        Intent intent = new Intent(context, ShopOpenDomainActivity.class);
        intent.putExtra(EXTRA_LOGOUT_ON_BACK, isLogoutOnBack);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_LOGOUT_ON_BACK)) {
            isLogoutOnBack = getIntent().getBooleanExtra(EXTRA_LOGOUT_ON_BACK, false);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupFragment(Bundle savedInstance) {
        //TODO Check if user has reserved the domain, if already reserved, go to shop open mandatory
        ShopOpenDomainComponent shopOpenDomainComponent = getComponent();
        shopOpenDomainComponent.inject(this);

        //TODO reserve domain or not
        shopCheckIsReservePresenter.attachView(this);
        //TODO loading here
        shopCheckIsReservePresenter.isReservingDomain();

    }

    @Override
    public void onSuccessCheckReserveDomain(Object object) {
        //TODO hide loading
        //TODO setReservingDomain here
        //it is stub
        isReservingDomain = false;
        if (isReservingDomain) {
            //TODO go to next step
            Intent intent = new Intent(this, ShopOpenMandatoryActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            inflateFragment();
        }
    }

    @Override
    public void onErrorCheckReserveDomain(Throwable t) {
        //TODO hide loading, assume user has no domain yet
        inflateFragment();
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopOpenDomainFragment.newInstance();
    }

    @Override
    public ShopOpenDomainComponent getComponent() {
        if (shopOpenDomainComponent == null) {
            shopOpenDomainComponent = DaggerShopOpenDomainComponent
                    .builder()
                    .shopOpenDomainModule(new ShopOpenDomainModule())
                    .shopComponent(((SellerModuleRouter) getApplication()).getShopComponent())
                    .build();
        }
        return shopOpenDomainComponent;
    }

    @Override
    public void onBackPressed() {
        if (isLogoutOnBack) {
            SessionHandler session = new SessionHandler(this);
            session.Logout(this);
        } else {
            super.onBackPressed();
        }
    }


}