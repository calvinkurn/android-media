package com.tokopedia.discovery.newdiscovery.data.source;

import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.newdiscovery.data.mapper.ShopMapper;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.viewmodel.ShopViewModel;

import rx.Observable;

/**
 * Created by henrypriyono on 10/13/17.
 */

public class ShopDataSource {
    private final ShopMapper shopMapper;
    private final BrowseApi searchApi;

    public ShopDataSource(BrowseApi searchApi, ShopMapper shopMapper) {
        this.searchApi = searchApi;
        this.shopMapper = shopMapper;
    }

    public Observable<ShopViewModel> getShop(TKPDMapParam<String, Object> param) {
        return searchApi.browseShops(param).map(shopMapper);
    }
}
