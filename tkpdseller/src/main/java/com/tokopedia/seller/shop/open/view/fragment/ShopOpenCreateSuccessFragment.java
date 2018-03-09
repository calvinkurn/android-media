package com.tokopedia.seller.shop.open.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.fragment.BasePresenterFragment;
import com.tokopedia.seller.common.imageeditor.view.WatermarkPresenterView;
import com.tokopedia.seller.product.edit.view.activity.ProductAddActivity;
import com.tokopedia.seller.shop.open.analytic.ShopOpenTracking;
import com.tokopedia.seller.shop.open.di.component.DaggerShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.view.presenter.ShopOpenCreateSuccessPresenter;

import javax.inject.Inject;


public class ShopOpenCreateSuccessFragment extends BasePresenterFragment implements WatermarkPresenterView {
    public static final String TAG = ShopOpenCreateSuccessFragment.class.getSimpleName();

    @Inject
    public ShopOpenCreateSuccessPresenter shopCreateSuccessPresenter;
    private ImageView shopIconImageView;
    private LoadingStateView loadingStateView;
    private TextView tvShopName;

    @Inject
    ShopOpenTracking trackingOpenShop;

    public static ShopOpenCreateSuccessFragment newInstance() {
        return new ShopOpenCreateSuccessFragment();
    }

    @Override
    protected void initInjector() {
        ShopOpenDomainComponent shopOpenDomainComponent = DaggerShopOpenDomainComponent.builder()
                .shopComponent(((SellerModuleRouter)MainApplication.getInstance()).getShopComponent())
                .build();
        shopOpenDomainComponent.inject(this);
        shopCreateSuccessPresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_create_success, container, false);
        loadingStateView = view.findViewById(R.id.loading_state_view);
        shopIconImageView = view.findViewById(R.id.image_view_shop_icon);
        tvShopName = view.findViewById(R.id.text_view_shop_title);
        View buttonAddProduct = view.findViewById(R.id.button_add_product);
        View buttonToShopPage = view.findViewById(R.id.button_shop_page);
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackingOpenShop.eventOpenShopThanksClickAddProduct();
                ProductAddActivity.start(getActivity());
                getActivity().finish();
            }
        });
        buttonToShopPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackingOpenShop.eventOpenShopThanksGoToMyShop();
                Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        shopCreateSuccessPresenter.attachView(this);
        showLoading();
        shopCreateSuccessPresenter.getShopInfo();
    }

    private void showLoading(){
        loadingStateView.setViewState(LoadingStateView.VIEW_LOADING);
    }

    private void hideLoading(){
        loadingStateView.setViewState(LoadingStateView.VIEW_CONTENT);
    }

    @Override
    public void onPause() {
        super.onPause();
        shopCreateSuccessPresenter.detachView();
    }

    @Override
    public void onSuccessGetShopInfo(ShopModel shopModel) {
        hideLoading();
        if (!TextUtils.isEmpty(shopModel.info.shopAvatar)) {
            ImageHandler.LoadImage(shopIconImageView, shopModel.info.shopAvatar);
        } else {
            shopIconImageView.setImageResource(R.drawable.ic_placeholder_shop_with_padding);
        }
        tvShopName.setText(MethodChecker.fromHtml(shopModel.info.getShopName()));
    }

    @Override
    public void onErrorGetShopInfo(Throwable t) {
        if(!GlobalConfig.DEBUG) Crashlytics.logException(t);
        loadingStateView.setErrorViewRes(R.layout.design_retry);
        View errorView = loadingStateView.getErrorView();
        errorView.findViewById(R.id.retry_but).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                shopCreateSuccessPresenter.getShopInfo();
            }
        });
        loadingStateView.setViewState(LoadingStateView.VIEW_ERROR);
    }

}