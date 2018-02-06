package com.tokopedia.shop.info.view.fragment;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.info.data.source.cloud.model.ShopNote;
import com.tokopedia.shop.info.di.component.DaggerShopInfoComponent;
import com.tokopedia.shop.info.di.module.ShopInfoModule;
import com.tokopedia.shop.info.view.listener.ShopNoteListView;
import com.tokopedia.shop.info.view.presenter.ShopNoteListPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by nathan on 2/5/18.
 */

public class ShopNoteListFragment extends BaseDaggerFragment implements ShopNoteListView {

    public static ShopNoteListFragment createInstance(String shopId) {
        ShopNoteListFragment shopNoteListFragment = new ShopNoteListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ShopParamConstant.SHOP_ID, shopId);
        shopNoteListFragment.setArguments(bundle);
        return shopNoteListFragment;
    }

    @Inject
    ShopNoteListPresenter shopNoteListPresenter;
    private String shopId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId = getArguments().getString(ShopParamConstant.SHOP_ID);
        shopNoteListPresenter.attachView(this);
        shopNoteListPresenter.getShopNoteList(shopId);
    }

    @Override
    public void onSuccessGetShopNoteList(List<ShopNote> shopNoteList) {

    }

    @Override
    public void onErrorGetShopNoteList(Throwable e) {

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
        if (shopNoteListPresenter != null) {
            shopNoteListPresenter.detachView();
        }
    }
}
