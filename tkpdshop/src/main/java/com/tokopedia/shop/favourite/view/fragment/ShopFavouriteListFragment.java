package com.tokopedia.shop.favourite.view.fragment;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModelShimmeringList;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.analytic.ShopPageTracking;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.favourite.di.component.DaggerShopFavouriteComponent;
import com.tokopedia.shop.favourite.di.module.ShopFavouriteModule;
import com.tokopedia.shop.favourite.view.adapter.ShopFavouriteAdapterTypeFactory;
import com.tokopedia.shop.favourite.view.listener.ShopFavouriteListView;
import com.tokopedia.shop.favourite.view.model.ShopFavouriteViewModel;
import com.tokopedia.shop.favourite.view.presenter.ShopFavouriteListPresenter;

import javax.inject.Inject;

/**
 * Created by nathan on 2/5/18.
 */

public class ShopFavouriteListFragment extends BaseListFragment<ShopFavouriteViewModel, ShopFavouriteAdapterTypeFactory> implements ShopFavouriteListView {

    public static ShopFavouriteListFragment createInstance(String shopId) {
        ShopFavouriteListFragment shopFavouriteListFragment = new ShopFavouriteListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ShopParamConstant.EXTRA_SHOP_ID, shopId);
        shopFavouriteListFragment.setArguments(bundle);
        return shopFavouriteListFragment;
    }

    @Inject
    ShopFavouriteListPresenter shopFavouriteListPresenter;
    @Inject
    ShopPageTracking shopPageTracking;
    private String shopId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId = getArguments().getString(ShopParamConstant.EXTRA_SHOP_ID);
        shopFavouriteListPresenter.attachView(this);
    }

    @Override
    public void loadData(int page) {
        shopFavouriteListPresenter.getshopFavouriteList(shopId, page);
    }

    @Override
    public LoadingModel getLoadingModel() {
        if(isLoadingInitialData){
            return new LoadingModelShimmeringList();
        }else{
            return super.getLoadingModel();
        }
    }

    @Override
    protected ShopFavouriteAdapterTypeFactory getAdapterTypeFactory() {
        return new ShopFavouriteAdapterTypeFactory();
    }

    @Override
    public void onItemClicked(ShopFavouriteViewModel shopFavouriteViewModel) {
        shopPageTracking.eventClickUserFavouritingShop(shopId);
        ((ShopModuleRouter) getActivity().getApplication()).goToProfileShop(getActivity(), shopFavouriteViewModel.getId());
    }

    @Override
    protected void initInjector() {
        DaggerShopFavouriteComponent
                .builder()
                .shopFavouriteModule(new ShopFavouriteModule())
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
        shopPageTracking.eventCloseListFavourite(shopId);
        if (shopFavouriteListPresenter != null) {
            shopFavouriteListPresenter.detachView();
        }
    }
}
