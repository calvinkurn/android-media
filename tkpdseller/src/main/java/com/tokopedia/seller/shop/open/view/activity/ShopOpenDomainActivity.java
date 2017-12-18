package com.tokopedia.seller.shop.open.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.shop.open.di.component.DaggerShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.di.module.ShopOpenDomainModule;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenDomainFragment;
import com.tokopedia.seller.shop.open.view.listener.ShopCheckDomainView;
import com.tokopedia.seller.shop.open.view.presenter.ShopCheckIsReservePresenterImpl;
import com.tokopedia.seller.shop.setting.data.model.response.ResponseIsReserveDomain;

import javax.inject.Inject;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopOpenDomainActivity extends BaseSimpleActivity
        implements HasComponent<ShopOpenDomainComponent>, ShopCheckDomainView,
        ShopOpenDomainFragment.OnShopOpenDomainFragmentListener{
    public static final String EXTRA_LOGOUT_ON_BACK = "LOGOUT_ON_BACK";

    private ShopOpenDomainComponent shopOpenDomainComponent;

    TkpdProgressDialog tkpdProgressDialog;

    boolean isLogoutOnBack = false;

    @Inject
    ShopCheckIsReservePresenterImpl shopCheckIsReservePresenter;
    private ShopOpenDomainFragment shopOpenDomainFragment;

    public static Intent getIntent(Context context, boolean isLogoutOnBack) {
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
        setUpPresenter();
    }

    private void setUpPresenter(){
        ShopOpenDomainComponent shopOpenDomainComponent = getComponent();
        shopOpenDomainComponent.inject(this);
    }

    @Override
    protected void setupFragment(Bundle savedInstance) {
        // no op, handled in onResume
    }

    @Override
    public void onSuccessCheckReserveDomain(ResponseIsReserveDomain responseIsReserveDomain) {
        hideLoading();
        boolean isReservingDomain = responseIsReserveDomain.isDomainAlreadyReserved();
        if (isReservingDomain) {
            goToShopOpenMandatory();
        } else {
            inflateFragment();
        }
    }

    private void goToShopOpenMandatory() {
        Intent intent = ShopOpenMandatoryActivity.getIntent(this,isLogoutOnBack);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onErrorCheckReserveDomain(Throwable t) {
        //hide loading, assume user has no domain yet
        hideLoading();
        inflateFragment();
    }

    @Override
    protected Fragment getNewFragment() {
        shopOpenDomainFragment = ShopOpenDomainFragment.newInstance();
        return shopOpenDomainFragment;
    }

    @Override
    protected void onResume() {
        super.onResume();
        shopCheckIsReservePresenter.attachView(this);
        if (shopOpenDomainFragment == null) {
            showLoading();
            shopCheckIsReservePresenter.isReservingDomain();
        }
    }

    private void hideLoading(){
        if (tkpdProgressDialog!= null) {
            tkpdProgressDialog.dismiss();
        }
    }

    private void showLoading(){
        if (tkpdProgressDialog== null) {
            tkpdProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS,
                    getString(R.string.title_loading));
        }
        tkpdProgressDialog.showDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shopCheckIsReservePresenter.detachView();
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
    public void onSuccessReserveShop() {
        goToShopOpenMandatory();
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