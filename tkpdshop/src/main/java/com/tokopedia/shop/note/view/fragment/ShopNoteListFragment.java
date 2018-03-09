package com.tokopedia.shop.note.view.fragment;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.note.di.component.DaggerShopNoteComponent;
import com.tokopedia.shop.note.di.module.ShopNoteModule;
import com.tokopedia.shop.note.view.activity.ShopNoteDetailActivity;
import com.tokopedia.shop.note.view.adapter.ShopNoteAdapterTypeFactory;
import com.tokopedia.shop.note.view.listener.ShopNoteListView;
import com.tokopedia.shop.note.view.model.ShopNoteViewModel;
import com.tokopedia.shop.note.view.presenter.ShopNoteListPresenter;
import com.tokopedia.shop.page.view.activity.ShopPageActivity;

import javax.inject.Inject;

/**
 * Created by nathan on 2/5/18.
 */

public class ShopNoteListFragment extends BaseListFragment<ShopNoteViewModel, ShopNoteAdapterTypeFactory> implements ShopNoteListView, EmptyViewHolder.Callback {

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
    }

    @Override
    protected ShopNoteAdapterTypeFactory getAdapterTypeFactory() {
        return new ShopNoteAdapterTypeFactory(this);
    }

    @Override
    protected Visitable getEmptyDataViewModel() {
        EmptyModel emptyModel = new EmptyModel();
        if (shopNoteListPresenter.isMyShop(shopId)) {
            emptyModel.setTitle(getString(R.string.shop_note_empty_note_title_owner));
            emptyModel.setContent(getString(R.string.shop_note_empty_note_content_owner));
            emptyModel.setButtonTitle(getString(R.string.shop_note_empty_note_button_owner));
        } else {
            emptyModel.setContent(getString(R.string.shop_note_empty_note_title));
        }
        return emptyModel;
    }

    @Override
    public void loadData(int page) {
        shopNoteListPresenter.getShopNoteList(shopId);
    }

    @Override
    public void onItemClicked(ShopNoteViewModel shopNoteViewModel) {
        startActivity(ShopNoteDetailActivity.createIntent(getActivity(), Long.toString(shopNoteViewModel.getShopNoteId())));
    }

    @Override
    public void onEmptyContentItemTextClicked() {

    }

    @Override
    public void onEmptyButtonClicked() {
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
