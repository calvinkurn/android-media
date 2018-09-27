package com.tokopedia.seller.shopsettings.notes.presenter;

import com.tokopedia.core.manage.shop.notes.model.ShopNote;

/**
 * Created by nisie on 10/26/16.
 * use ShopSettings Module
 */
@Deprecated
public interface ManageShopNotesPresenter {

    void initData();

    void onRefresh();

    void onDestroyView();

    void onDeleteNote(ShopNote shopNote);

    void addNote();

    void addReturnablePolicy();
}
