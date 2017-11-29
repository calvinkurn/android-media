package com.tokopedia.discovery.newdiscovery.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.newdiscovery.data.source.ShopDataSource;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.viewmodel.ShopViewModel;

import rx.Observable;

/**
 * Created by henrypriyono on 10/13/17.
 */

public class ShopRepositoryImpl implements ShopRepository {

    private ShopDataSource shopDataSource;

    public ShopRepositoryImpl(ShopDataSource shopDataSource) {
        this.shopDataSource = shopDataSource;
    }

    @Override
    public Observable<ShopViewModel> getShop(TKPDMapParam<String, Object> parameters) {
        return shopDataSource.getShop(parameters);
    }
}
