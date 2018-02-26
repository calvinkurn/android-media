package com.tokopedia.shop.product.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent;
import com.tokopedia.shop.product.di.module.ShopProductModule;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.ShopProductLimitedAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.ShopProductTypeFactory;
import com.tokopedia.shop.product.view.listener.ShopProductListLimitedView;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductSingleViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.model.ShopProductBaseViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.shop.product.view.presenter.ShopProductListLimitedPresenter;

import javax.inject.Inject;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductListLimitedFragment extends
        BaseListFragment<ShopProductBaseViewModel, ShopProductLimitedAdapterTypeFactory> implements ShopProductListLimitedView {

    public static ShopProductListLimitedFragment createInstance() {
        ShopProductListLimitedFragment fragment = new ShopProductListLimitedFragment();
        return fragment;
    }

    @Inject
    ShopProductListLimitedPresenter shopProductListLimitedPresenter;
    private String shopId;

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
        return new ShopProductLimitedAdapterTypeFactory(new ShopProductAdapterTypeFactory.TypeFactoryListener() {
            @Override
            public int getType(Object type) {
                return ShopProductSingleViewHolder.LAYOUT;
            }
        }, new ShopProductViewHolder.ShopProductVHListener() {
            @Override
            public void onWishlist(ShopProductViewModel model) {
                shopProductListLimitedPresenter.addToWishList(
                        shopId,
                        model.getId()
                );
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
//        shopProductListLimitedPresenter.getShopPageList(shopId);
        shopProductListLimitedPresenter.getProductLimitedList(shopId);
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
}
