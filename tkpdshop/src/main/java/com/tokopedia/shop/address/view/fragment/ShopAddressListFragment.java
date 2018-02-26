package com.tokopedia.shop.address.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.shop.R;
import com.tokopedia.shop.address.view.adapter.ShopAddressAdapterTypeFactory;
import com.tokopedia.shop.address.view.listener.ShopAddressListView;
import com.tokopedia.shop.address.view.model.ShopAddressViewModel;
import com.tokopedia.shop.address.view.presenter.ShopAddressListPresenter;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.info.di.component.DaggerShopInfoComponent;
import com.tokopedia.shop.info.di.module.ShopInfoModule;

import javax.inject.Inject;

/**
 * Created by nathan on 2/5/18.
 */

public class ShopAddressListFragment extends BaseListFragment<ShopAddressViewModel, ShopAddressAdapterTypeFactory> implements ShopAddressListView {

    public static ShopAddressListFragment createInstance(String shopId) {
        ShopAddressListFragment shopAddressListFragment = new ShopAddressListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ShopParamConstant.SHOP_ID, shopId);
        shopAddressListFragment.setArguments(bundle);
        return shopAddressListFragment;
    }

    @Inject
    ShopAddressListPresenter shopAddressListPresenter;
    private String shopId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId = getArguments().getString(ShopParamConstant.SHOP_ID);
        shopAddressListPresenter.attachView(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        VerticalRecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.clearItemDecoration();
    }

    @Override
    public void loadData(int page) {
        shopAddressListPresenter.getshopAddressList(shopId);
    }

    @Override
    protected ShopAddressAdapterTypeFactory getAdapterTypeFactory() {
        return new ShopAddressAdapterTypeFactory();
    }

    @Override
    public void onItemClicked(ShopAddressViewModel shopAddressViewModel) {

    }

    @Override
    protected void initInjector() {
        DaggerShopInfoComponent
                .builder()
                .shopInfoModule(new ShopInfoModule())
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
        if (shopAddressListPresenter != null) {
            shopAddressListPresenter.detachView();
        }
    }
}
