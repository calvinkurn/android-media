package com.tokopedia.shop.product.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.shop.common.constant.ShopParamApiConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent;
import com.tokopedia.shop.product.di.module.ShopProductModule;
import com.tokopedia.shop.product.view.activity.ShopEtalaseActivity;
import com.tokopedia.shop.product.view.adapter.ShopEtalaseAdapterTypeFactory;
import com.tokopedia.shop.product.view.listener.ShopEtalaseFragmentListener;
import com.tokopedia.shop.product.view.listener.ShopEtalaseView;
import com.tokopedia.shop.product.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.view.presenter.ShopEtalasePresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class ShopEtalaseFragment extends BaseListFragment<ShopEtalaseViewModel, ShopEtalaseAdapterTypeFactory> implements ShopEtalaseView{
    public static final int DEFAULT_INDEX_SELECTION = 0;
    String shopId, shopDomain;

    @Inject
    ShopEtalasePresenter shopEtalasePresenter;
    private ShopEtalaseFragmentListener shopEtlaseFragmentListener;
    private String selectedEtalaseId;

    public static ShopEtalaseFragment createInstance(String shoId, String shopDomain, String selectedEtalaseId) {
        ShopEtalaseFragment fragment = new ShopEtalaseFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ShopParamApiConstant.SHOP_ID, shoId);
        arguments.putString(ShopParamApiConstant.SHOP_DOMAIN, shopDomain);
        arguments.putString(ShopEtalaseActivity.SELECTED_ETALASE_ID, selectedEtalaseId);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState == null){
            shopId = getArguments().getString(ShopParamApiConstant.SHOP_ID);
            shopDomain = getArguments().getString(ShopParamApiConstant.SHOP_DOMAIN);
            selectedEtalaseId = getArguments().getString(ShopEtalaseActivity.SELECTED_ETALASE_ID);
        }

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void loadData(int i) {
        shopEtalasePresenter.getShopEtalase(shopId, shopDomain);
    }

    @Override
    protected ShopEtalaseAdapterTypeFactory getAdapterTypeFactory() {
        return new ShopEtalaseAdapterTypeFactory();
    }

    @Override
    public void onItemClicked(ShopEtalaseViewModel shopEtalaseViewModel) {
        if (shopEtlaseFragmentListener != null) {
            shopEtlaseFragmentListener.select(shopEtalaseViewModel);
        }
    }

    @Override
    public void renderList(@NonNull List<ShopEtalaseViewModel> list, boolean isHasNext) {
        if(selectedEtalaseId != null){
            for(int i=0;i<list.size();i++)
                if(list.get(i).getEtalaseId().equalsIgnoreCase(selectedEtalaseId))
                    list.get(i).setSelected(true);
        }else{
            list.get(DEFAULT_INDEX_SELECTION).setSelected(true);
        }
        super.renderList(list);
    }

    @Override
    protected void initInjector() {
        DaggerShopProductComponent
                .builder()
                .shopProductModule(new ShopProductModule())
                .shopComponent(getComponent(ShopComponent.class))
                .build()
                .inject(this);
        shopEtalasePresenter.attachView(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null && context instanceof ShopEtalaseFragmentListener) {
            shopEtlaseFragmentListener = (ShopEtalaseFragmentListener) context;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        shopEtalasePresenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
