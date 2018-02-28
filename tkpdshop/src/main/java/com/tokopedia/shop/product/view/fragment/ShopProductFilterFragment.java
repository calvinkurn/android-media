package com.tokopedia.shop.product.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent;
import com.tokopedia.shop.product.di.module.ShopProductModule;
import com.tokopedia.shop.product.view.activity.ShopProductFilterActivity;
import com.tokopedia.shop.product.view.adapter.ShopProductFilterAdapterTypeFactory;
import com.tokopedia.shop.product.view.listener.ShopFilterListView;
import com.tokopedia.shop.product.view.listener.ShopProductFilterFragmentListener;
import com.tokopedia.shop.product.view.model.ShopProductFilterModel;
import com.tokopedia.shop.product.view.presenter.ShopProductFilterPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by normansyahputa on 2/23/18.
 */

public class ShopProductFilterFragment extends BaseListFragment<ShopProductFilterModel, ShopProductFilterAdapterTypeFactory> implements ShopFilterListView {

    @Inject
    ShopProductFilterPresenter shopProductFilterPresenter;
    private String sortName;
    private ShopProductFilterFragmentListener shopFilterFragmentListener;

    public static ShopProductFilterFragment createInstance(String sortName) {
        ShopProductFilterFragment fragment = new ShopProductFilterFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ShopProductFilterActivity.SORT_NAME, sortName);
        fragment.setArguments(arguments);
        return fragment;
    }

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
        if (context != null && context instanceof ShopProductFilterFragmentListener) {
            shopFilterFragmentListener = (ShopProductFilterFragmentListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopProductFilterPresenter.attachView(this);
    }

    @Override
    public void renderList(@NonNull List<ShopProductFilterModel> list, boolean hasNextPage) {
        if (sortName != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getValue().equalsIgnoreCase(sortName)) {
                    list.get(i).setSelected(true);
                }
            }
        } else {
            list.get(0).setSelected(true);
        }
        super.renderList(list, hasNextPage);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null && savedInstanceState == null) {
            sortName = getArguments().getString(ShopProductFilterActivity.SORT_NAME);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected ShopProductFilterAdapterTypeFactory getAdapterTypeFactory() {
        return new ShopProductFilterAdapterTypeFactory();
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
    public void onItemClicked(ShopProductFilterModel filterModel) {
        shopFilterFragmentListener.select(filterModel.getKey(), filterModel.getValue());
    }
}
