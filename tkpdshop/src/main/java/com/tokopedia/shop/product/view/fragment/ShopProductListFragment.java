package com.tokopedia.shop.product.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.shop.R;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.product.di.module.ShopProductModule;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.ShopProductTypeFactory;
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.shop.product.view.presenter.ShopProductListPresenter;

import javax.inject.Inject;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductListFragment extends BaseListFragment<ShopProductViewModel, ShopProductTypeFactory>  {

    public static final int SPAN_COUNT = 2;

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

    @Override
    public void onItemClicked(ShopProductViewModel shopProductViewModel) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_product_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT,  LinearLayoutManager.VERTICAL,
                false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (getAdapter().getItemViewType(position) == ShopProductViewHolder.LAYOUT){
                    return ShopProductViewHolder.SPAN_LOOK_UP;
                }
                return SPAN_COUNT;
            }
        });

        ((RecyclerView)view.findViewById(R.id.recycler_view)).setLayoutManager(gridLayoutManager);
    }
}
