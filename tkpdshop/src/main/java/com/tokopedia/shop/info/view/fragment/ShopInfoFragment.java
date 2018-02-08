package com.tokopedia.shop.info.view.fragment;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.info.di.component.DaggerShopInfoComponent;
import com.tokopedia.shop.info.di.module.ShopInfoModule;
import com.tokopedia.shop.info.view.listener.ShopInfoView;
import com.tokopedia.shop.info.view.presenter.ShopInfoPresenter;

import javax.inject.Inject;

/**
 * Created by nathan on 2/5/18.
 */

public class ShopInfoFragment extends BaseDaggerFragment implements ShopInfoView {

    public static ShopInfoFragment createInstance(String shopId) {
        ShopInfoFragment shopInfoFragment = new ShopInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ShopParamConstant.SHOP_ID, shopId);
        shopInfoFragment.setArguments(bundle);
        return shopInfoFragment;
    }

    @Inject
    ShopInfoPresenter shopInfoDetailPresenter;
    private String shopId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId = getArguments().getString(ShopParamConstant.SHOP_ID);
        shopInfoDetailPresenter.attachView(this);
        shopInfoDetailPresenter.getShopInfo(shopId);
    }

    @Override
    public void onSuccessGetShopInfo(ShopInfo shopInfo) {

    }

    @Override
    public void onErrorGetShopInfo(Throwable e) {

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
        if (shopInfoDetailPresenter != null) {
            shopInfoDetailPresenter.detachView();
        }
    }
}
