package com.tokopedia.shop.note.view.fragment;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.shop.analytic.ShopPageTracking;
import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.note.di.component.DaggerShopNoteComponent;
import com.tokopedia.shop.note.di.module.ShopNoteModule;
import com.tokopedia.shop.note.view.activity.ShopNoteDetailActivity;
import com.tokopedia.shop.note.view.adapter.ShopNoteAdapterTypeFactory;
import com.tokopedia.shop.note.view.listener.ShopNoteListView;
import com.tokopedia.shop.note.view.model.ShopNoteViewModel;
import com.tokopedia.shop.note.view.presenter.ShopNoteListPresenter;

import javax.inject.Inject;

/**
 * Created by nathan on 2/5/18.
 */

public class ShopNoteListFragment extends BaseListFragment<ShopNoteViewModel, ShopNoteAdapterTypeFactory> implements ShopNoteListView, EmptyViewHolder.Callback {

    public static ShopNoteListFragment createInstance() {
        ShopNoteListFragment shopNoteListFragment = new ShopNoteListFragment();
        return shopNoteListFragment;
    }

    @Inject
    ShopNoteListPresenter shopNoteListPresenter;
    @Inject
    ShopPageTracking shopPageTracking;
    private ShopInfo shopInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopNoteListPresenter.attachView(this);
    }

    @Override
    protected ShopNoteAdapterTypeFactory getAdapterTypeFactory() {
        return new ShopNoteAdapterTypeFactory(this);
    }

    @Override
    protected Visitable getEmptyDataViewModel() {
        EmptyModel emptyModel = new EmptyModel();
        if (shopNoteListPresenter.isMyShop(shopInfo.getInfo().getShopId())) {
            emptyModel.setTitle(getString(R.string.shop_note_empty_note_title_owner));
            emptyModel.setContent(getString(R.string.shop_note_empty_note_content_owner));
            emptyModel.setButtonTitle(getString(R.string.shop_note_empty_note_button_owner));
        } else {
            emptyModel.setContent(getString(R.string.shop_note_empty_note_title));
        }
        return emptyModel;
    }

    public void updateShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
        loadInitialData();
    }

    @Override
    public void loadData(int page) {
        if (shopInfo != null) {
            shopNoteListPresenter.getShopNoteList(shopInfo.getInfo().getShopId());
        }
    }

    @Override
    public void onItemClicked(ShopNoteViewModel shopNoteViewModel) {
        shopPageTracking.eventClickNoteList(shopNoteViewModel.getPosition(), shopInfo.getInfo().getShopId(),
                shopNoteListPresenter.isMyShop(shopInfo.getInfo().getShopId()), ShopPageTracking.getShopType(shopInfo.getInfo()));
        startActivity(ShopNoteDetailActivity.createIntent(getActivity(), Long.toString(shopNoteViewModel.getShopNoteId())));
    }

    @Override
    public void onEmptyContentItemTextClicked() {

    }

    @Override
    public void onEmptyButtonClicked() {
        shopPageTracking.eventClickAddNote(shopInfo.getInfo().getShopId());
        ((ShopModuleRouter) getActivity().getApplication()).goToEditShopNote(getActivity());
    }

    @Override
    protected void initInjector() {
        DaggerShopNoteComponent
                .builder()
                .shopNoteModule(new ShopNoteModule())
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
