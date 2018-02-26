package com.tokopedia.shop.product.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.product.di.module.ShopProductModule;
import com.tokopedia.shop.product.view.activity.ShopProductFilterActivity;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.ShopProductTypeFactory;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseSelectedViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseUnselectedViewHolder;
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent;
import com.tokopedia.shop.product.view.listener.ShopProductFilterFragmentListener;
import com.tokopedia.shop.product.view.model.ShopProductFilterModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.shop.product.view.presenter.ShopProductFilterPresenter;

import javax.inject.Inject;

/**
 * Created by normansyahputa on 2/23/18.
 */

public class ShopProductFilterFragment extends BaseListFragment<ShopProductViewModel, ShopProductTypeFactory> {

    private String sortName;

    public static ShopProductFilterFragment createInstance(String sortName){
        ShopProductFilterFragment fragment = new ShopProductFilterFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ShopProductFilterActivity.SORT_NAME, sortName);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Inject
    ShopProductFilterPresenter shopProductFilterPresenter;
    private ShopProductFilterFragmentListener shopFilterFragmentListener;

    @Override
    public void loadData(int i) {
        shopProductFilterPresenter.getShopFilterList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shopProductFilterPresenter != null) {
            shopProductFilterPresenter.detachView();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context != null && context instanceof ShopProductFilterFragmentListener){
            shopFilterFragmentListener = (ShopProductFilterFragmentListener)context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopProductFilterPresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getArguments()!= null && savedInstanceState == null){
            sortName = getArguments().getString(ShopProductFilterActivity.SORT_NAME);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected ShopProductTypeFactory getAdapterTypeFactory() {
        return new ShopProductAdapterTypeFactory(new ShopProductAdapterTypeFactory.TypeFactoryListener<ShopProductViewModel>() {
            @Override
            public int getType(ShopProductViewModel type) {
                if (type instanceof ShopProductFilterModel) {
                    ShopProductFilterModel filterModel = (ShopProductFilterModel) type;
                    if (sortName.contains(filterModel.getValue())) {
                        return ShopProductEtalaseSelectedViewHolder.LAYOUT;
                    } else {
                        return ShopProductEtalaseUnselectedViewHolder.LAYOUT;
                    }
                }
                return 0;
            }
        }, new ShopProductViewHolder.ShopProductVHListener() {
            @Override
            public void onWishlist(ShopProductViewModel model) {

            }
        });
    }

    @Override
    public void onItemClicked(ShopProductViewModel shopProductViewModel) {
        if(shopFilterFragmentListener != null && shopProductViewModel instanceof ShopProductFilterModel){

            ShopProductFilterModel filterModel = (ShopProductFilterModel) shopProductViewModel;

            shopFilterFragmentListener.select(filterModel.getKey(), filterModel.getValue());
        }
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
}
