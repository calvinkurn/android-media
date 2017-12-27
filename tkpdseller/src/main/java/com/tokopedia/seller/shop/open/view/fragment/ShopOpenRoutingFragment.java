package com.tokopedia.seller.shop.open.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.view.activity.ShopOpenDomainActivity;
import com.tokopedia.seller.shop.open.view.activity.ShopOpenMandatoryActivity;
import com.tokopedia.seller.shop.open.view.listener.ShopCheckDomainView;
import com.tokopedia.seller.shop.open.view.presenter.ShopCheckIsReservePresenterImpl;
import com.tokopedia.seller.shop.setting.data.model.response.ResponseIsReserveDomain;

import javax.inject.Inject;

/**
 * Created by Hendry on 3/17/2017.
 */

public class ShopOpenRoutingFragment extends BaseDaggerFragment implements ShopCheckDomainView {

    public static ShopOpenRoutingFragment newInstance() {
        return new ShopOpenRoutingFragment();
    }

    private View loadingLayout;
    private View errorLayout;
    private Button retryButton;

    @Inject
    ShopCheckIsReservePresenterImpl shopCheckIsReservePresenter;

    @Override
    protected void initInjector() {
        ShopOpenDomainComponent component = getComponent(ShopOpenDomainComponent.class);
        component.inject(this);
        shopCheckIsReservePresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_open_routing, container, false);
        loadingLayout = view.findViewById(R.id.layout_loading);
        errorLayout = view.findViewById(R.id.layout_error);
        retryButton = view.findViewById(R.id.button_retry);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading(true);
                shopCheckIsReservePresenter.isReservingDomain();
            }
        });
        shopCheckIsReservePresenter.isReservingDomain();
        return view;
    }

    @Override
    public void onSuccessCheckReserveDomain(ResponseIsReserveDomain responseIsReserveDomain) {
        boolean isReservingDomain = responseIsReserveDomain.isDomainAlreadyReserved();
        if (isReservingDomain) {
            goToShopOpenMandatory(responseIsReserveDomain);
        } else {
            goToShopOpenDomain();
        }
    }

    @Override
    public void onErrorCheckReserveDomain(Throwable t) {
        showLoading(false);
    }

    private void showLoading(boolean show) {
        loadingLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        errorLayout.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void goToShopOpenDomain() {
        Intent intent = ShopOpenDomainActivity.getIntent(getActivity());
        startActivity(intent);
        getActivity().finish();
    }

    private void goToShopOpenMandatory(ResponseIsReserveDomain responseIsReserveDomain) {
        Intent intent = ShopOpenMandatoryActivity.getIntent(getActivity(), responseIsReserveDomain);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}