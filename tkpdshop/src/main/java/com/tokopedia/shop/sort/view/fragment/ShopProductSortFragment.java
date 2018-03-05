package com.tokopedia.shop.sort.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSort;
import com.tokopedia.shop.sort.di.component.DaggerShopProductSortComponent;
import com.tokopedia.shop.product.di.module.ShopProductModule;
import com.tokopedia.shop.sort.di.module.ShopProductSortModule;
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity;
import com.tokopedia.shop.sort.view.adapter.ShopProductSortAdapterTypeFactory;
import com.tokopedia.shop.sort.view.listener.ShopProductSortListView;
import com.tokopedia.shop.sort.view.listener.ShopProductSortFragmentListener;
import com.tokopedia.shop.sort.view.model.ShopProductSortModel;
import com.tokopedia.shop.sort.view.presenter.ShopProductSortPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by normansyahputa on 2/23/18.
 */

public class ShopProductSortFragment extends BaseListFragment<ShopProductSortModel, ShopProductSortAdapterTypeFactory> implements ShopProductSortListView {

    @Inject
    ShopProductSortPresenter shopProductFilterPresenter;
    private String sortName;
    private ShopProductSortFragmentListener shopFilterFragmentListener;

    public static ShopProductSortFragment createInstance(String sortName) {
        ShopProductSortFragment fragment = new ShopProductSortFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ShopProductSortActivity.SORT_NAME, sortName);
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
        if (context != null && context instanceof ShopProductSortFragmentListener) {
            shopFilterFragmentListener = (ShopProductSortFragmentListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopProductFilterPresenter.attachView(this);
    }

    @Override
    public void renderList(@NonNull List<ShopProductSortModel> list, boolean hasNextPage) {
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
            sortName = getArguments().getString(ShopProductSortActivity.SORT_NAME);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected ShopProductSortAdapterTypeFactory getAdapterTypeFactory() {
        return new ShopProductSortAdapterTypeFactory();
    }

    @Override
    protected void initInjector() {
        DaggerShopProductSortComponent
                .builder()
                .shopProductSortModule(new ShopProductSortModule())
                .shopComponent(getComponent(ShopComponent.class))
                .build()
                .inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onItemClicked(ShopProductSortModel filterModel) {
        shopFilterFragmentListener.select(filterModel.getKey(), filterModel.getValue());
    }
}
