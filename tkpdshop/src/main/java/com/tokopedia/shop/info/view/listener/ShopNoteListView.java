package com.tokopedia.shop.info.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.shop.info.data.source.cloud.model.ShopNote;

import java.util.List;

public interface ShopNoteListView extends CustomerView {

    void onSuccessGetShopNoteList(List<ShopNote> shopNoteList);

    void onErrorGetShopNoteList(Throwable e);
}
