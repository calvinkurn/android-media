package com.tokopedia.discovery.newdiscovery.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.viewmodel.ShopViewModel;

import rx.Observable;

/**
 * Created by henrypriyono on 10/13/17.
 */

public interface ShopRepository {
    Observable<ShopViewModel> getShop(TKPDMapParam<String, Object> param);
}
