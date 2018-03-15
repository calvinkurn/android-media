package com.tokopedia.shop.favourite.view.listener;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.favourite.view.model.ShopFavouriteViewModel;

public interface ShopFavouriteListView extends BaseListViewListener<ShopFavouriteViewModel> {

    void onSuccessGetShopInfo(ShopInfo shopInfo);
}
