package com.tokopedia.shop.note.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.shop.ShopComponentInstance;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNoteDetail;
import com.tokopedia.shop.note.di.component.DaggerShopNoteComponent;
import com.tokopedia.shop.note.di.module.ShopNoteModule;
import com.tokopedia.shop.note.view.listener.ShopNoteDetailView;
import com.tokopedia.shop.note.view.presenter.ShopNoteDetailPresenter;

import javax.inject.Inject;

/**
 * Created by normansyahputa on 2/8/18.
 */

public class ShopNoteDetailFragment extends BaseDaggerFragment implements ShopNoteDetailView {

    @Inject
    ShopNoteDetailPresenter shopNoteDetailPresenter;
    private String shopNoteId;

    public static ShopNoteDetailFragment newInstance(String noteId){
        ShopNoteDetailFragment shopNoteListFragment = new ShopNoteDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ShopParamConstant.SHOP_NOTE_ID, noteId);
        shopNoteListFragment.setArguments(bundle);
        return shopNoteListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopNoteId = getArguments().getString(ShopParamConstant.SHOP_NOTE_ID);
        shopNoteDetailPresenter.attachView(this);
        shopNoteDetailPresenter.getShopNoteList(shopNoteId);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    protected void initInjector() {
        DaggerShopNoteComponent
                .builder()
                .shopNoteModule(new ShopNoteModule())
                .shopComponent( ShopComponentInstance.getComponent(getActivity().getApplication()))
                .build()
                .inject(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        shopNoteDetailPresenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void showError(Throwable e) {

    }

    @Override
    public void render(ShopNoteDetail shopNoteDetail) {

    }
}
