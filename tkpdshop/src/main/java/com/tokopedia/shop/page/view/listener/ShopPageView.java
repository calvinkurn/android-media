package com.tokopedia.shop.page.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeed;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.page.view.model.ShopPageViewModel;

/**
 * Created by normansyahputa on 2/13/18.
 */

public interface ShopPageView extends CustomerView {

    void onSuccessGetShopPageInfo(ShopPageViewModel shopPageViewModel);

    void onErrorGetShopPageInfo(Throwable e);

    void onSuccessToggleFavourite(boolean successValue);

    void onErrorToggleFavourite(Throwable e);
}
