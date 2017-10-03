package com.tokopedia.seller.product.edit.data.source;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.common.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.seller.product.edit.data.source.cloud.ShopInfoCloud;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;

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
