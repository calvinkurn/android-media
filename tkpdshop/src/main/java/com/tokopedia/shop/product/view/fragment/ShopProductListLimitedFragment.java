package com.tokopedia.shop.product.view.fragment;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent;
import com.tokopedia.shop.product.di.module.ShopProductModule;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.ShopProductTypeFactory;
import com.tokopedia.shop.product.view.listener.ShopProductListLimitedView;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.shop.product.view.presenter.ShopProductListLimitedPresenter;

import javax.inject.Inject;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductListLimitedFragment extends BaseListFragment<ShopProductViewModel, ShopProductTypeFactory> {

    public static ShopProductListLimitedFragment createInstance(String shopId) {
        ShopProductListLimitedFragment fragment = new ShopProductListLimitedFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ShopParamConstant.SHOP_ID, shopId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Inject
    ShopProductListLimitedPresenter shopProductListLimitedPresenter;
    private String shopId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId = getArguments().getString(ShopParamConstant.SHOP_ID);
        shopProductListLimitedPresenter.attachView(this);
    }

    @Override
    public void loadData(int i) {
        shopProductListLimitedPresenter.getShopPageList(shopId);
        shopProductListLimitedPresenter.getFeatureProductList(shopId);
    }

    @Override
    protected ShopProductTypeFactory getAdapterTypeFactory() {
        return new ShopProductAdapterTypeFactory(null);
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
        if (shopProductListLimitedPresenter != null) {
            shopProductListLimitedPresenter.detachView();
        }
    }

    @Override
    public void onItemClicked(ShopProductViewModel shopProductViewModel) {

    }
}
