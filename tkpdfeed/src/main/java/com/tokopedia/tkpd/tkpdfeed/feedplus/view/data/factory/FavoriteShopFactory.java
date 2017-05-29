package com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.factory;

import android.content.Context;

import com.tokopedia.core.shopinfo.facades.ActionShopInfoRetrofit;
import com.tokopedia.core.shopinfo.facades.authservices.ActionService;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FavoriteShopMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.source.cloud.FavoriteShopDataSource;

/**
 * Created by stevenfredian on 5/26/17.
 */

public class FavoriteShopFactory {
    private Context context;
    private FavoriteShopMapper mapper;
    private ActionService service;

    public FavoriteShopFactory(Context context, FavoriteShopMapper mapper, ActionService service) {
        this.context = context;
        this.mapper = mapper;
        this.service = service;
    }

    public FavoriteShopDataSource createCloudDoFavoriteShop() {
        return new FavoriteShopDataSource(context, service, mapper);
    }
}
