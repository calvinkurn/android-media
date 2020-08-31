package com.tokopedia.seller.manageitem.data.source;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.manageitem.common.mapper.SimpleDataResponseMapper;
import com.tokopedia.seller.manageitem.data.cloud.ShopInfoCloud;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 3/8/17.
 */
public class ShopInfoDataSource {
    private final ShopInfoCloud shopInfoCloud;
    private final SimpleDataResponseMapper<ShopModel> mapper;

    @Inject
    public ShopInfoDataSource(ShopInfoCloud shopInfoCloud,
                              SimpleDataResponseMapper<ShopModel> mapper) {
        this.shopInfoCloud = shopInfoCloud;
        this.mapper = mapper;
    }

    public Observable<ShopModel> getShopInfo() {
        return shopInfoCloud.getShopInfo()
                .map(mapper);
    }

}
