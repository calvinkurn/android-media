package com.tokopedia.shop.note.view.fragment;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.shop.ShopComponentInstance;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.note.di.component.DaggerShopNoteComponent;
import com.tokopedia.shop.note.di.module.ShopNoteModule;
import com.tokopedia.shop.note.view.presenter.ShopNoteDetailPresenter;

import javax.inject.Inject;

/**
 * Created by normansyahputa on 2/8/18.
 */

public class ShopNoteDetailFragment extends BaseDaggerFragment {

    @Inject
    ShopNoteDetailPresenter shopNoteDetailPresenter;

    public static ShopNoteDetailFragment newInstance(String noteId){
        ShopNoteDetailFragment shopNoteListFragment = new ShopNoteDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ShopParamConstant.SHOP_NOTE_ID, noteId);
        shopNoteListFragment.setArguments(bundle);
        return shopNoteListFragment;
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
    protected String getScreenName() {
        return null;
    }
}
