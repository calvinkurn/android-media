package com.tokopedia.shop.product.view.fragment;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.product.di.module.ShopProductModule;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.ShopProductTypeFactory;
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent;
import com.tokopedia.shop.product.view.listener.ShopProductListView;
import com.tokopedia.shop.product.view.presenter.ShopProductListPresenter;

import javax.inject.Inject;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductListFragment extends BaseListFragment<Visitable, ShopProductTypeFactory> implements ShopProductListView {

    public static ShopProductListFragment createInstance(String shopId) {
        ShopProductListFragment shopProductListFragment = new ShopProductListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ShopParamConstant.SHOP_ID, shopId);
        shopProductListFragment.setArguments(bundle);
        return shopProductListFragment;
    }


    @Inject
    ShopProductListPresenter shopProductListPresenter;
    private String shopId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId = getArguments().getString(ShopParamConstant.SHOP_ID);
        shopProductListPresenter.attachView(this);
    }

    @Override
    public void loadData(int page) {
        shopProductListPresenter.getShopPageList(shopId);
    }

    @Override
    public void onItemClicked(Visitable visitable) {

    }

    @Override
    protected ShopProductTypeFactory getAdapterTypeFactory() {
        return new ShopProductAdapterTypeFactory();
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

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shopProductListPresenter != null) {
            shopProductListPresenter.detachView();
        }
    }
}
