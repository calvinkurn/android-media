package com.tokopedia.shop.product.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent;
import com.tokopedia.shop.product.di.module.ShopProductModule;
import com.tokopedia.shop.product.view.activity.ShopEtalaseActivity;
import com.tokopedia.shop.product.view.activity.ShopProductListActivity;
import com.tokopedia.shop.product.view.adapter.ShopProductLimitedAdapterTypeFactory;
import com.tokopedia.shop.product.view.listener.ShopProductListLimitedView;
import com.tokopedia.shop.product.view.model.ShopProductBaseViewModel;
import com.tokopedia.shop.product.view.presenter.ShopProductListLimitedPresenter;

import javax.inject.Inject;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductListLimitedFragment extends BaseSearchListFragment<ShopProductBaseViewModel, ShopProductLimitedAdapterTypeFactory>
        implements ShopProductListLimitedView {


    @Inject
    ShopProductListLimitedPresenter shopProductListLimitedPresenter;
    private String shopId;

    public static ShopProductListLimitedFragment createInstance() {
        ShopProductListLimitedFragment fragment = new ShopProductListLimitedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopProductListLimitedPresenter.attachView(this);
    }

    @Override
    public void loadData(int i) {
        displayProduct(shopId, null);
    }

    @NonNull
    @Override
    protected ShopProductLimitedAdapterTypeFactory getAdapterTypeFactory() {
        return new ShopProductLimitedAdapterTypeFactory(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ShopProductListActivity.createIntent(getActivity(), shopId));
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ShopEtalaseActivity.createIntent(getActivity(), shopId, null));
            }
        });
    }

    @Override
    protected void initInjector() {
        DaggerShopProductComponent
                .builder()
                .shopProductModule(new ShopProductModule())
                .shopComponent(getComponent(ShopComponent.class))
                .build()
                .inject(this);
    }

    public void displayProduct(String shopId, String promotionWebViewUrl) {
        this.shopId = shopId;
        shopProductListLimitedPresenter.getProductLimitedList(shopId, promotionWebViewUrl);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shopProductListLimitedPresenter != null) {
            shopProductListLimitedPresenter.detachView();
        }
    }

    @Override
    public void onItemClicked(ShopProductBaseViewModel shopProductBaseViewModel) {

    }

    @Override
    public void onSearchSubmitted(String text) {
        startActivity(ShopProductListActivity.createIntent(
                getActivity(),
                shopId,
                text,
                null,
                null
        ));
    }

    @Override
    public void onSearchTextChanged(String text) {

    }
}
