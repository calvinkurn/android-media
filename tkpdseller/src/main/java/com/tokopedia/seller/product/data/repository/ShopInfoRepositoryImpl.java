package com.tokopedia.seller.product.data.repository;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.product.data.source.ShopInfoDataSource;
import com.tokopedia.seller.product.domain.ShopInfoRepository;
import com.tokopedia.seller.product.domain.model.AddProductShopInfoDomainModel;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author hendry on 4/20/17.
 */

public class ShopInfoRepositoryImpl implements ShopInfoRepository {
    private final ShopInfoDataSource shopInfoDataSource;

    @Inject
    public ShopInfoRepositoryImpl(ShopInfoDataSource shopInfoDataSource) {
        this.shopInfoDataSource = shopInfoDataSource;
    }

    @Override
    public Observable<ShopModel> getShopInfo(String shopId, String shopDomain) {
        return shopInfoDataSource.getShopInfo(shopId, shopDomain);
    }

    @Override
    public Observable<ShopModel> getShopInfoFromNetwork(String shopId, String shopDomain) {
        return shopInfoDataSource.getShopInfoFromNetwork(shopId, shopDomain);
    }

    @Override
    public Observable<AddProductShopInfoDomainModel> getAddProductShopInfo(String shopId, String shopDomain) {
        return shopInfoDataSource.getShopInfo(shopId, shopDomain).map(new Func1<ShopModel, AddProductShopInfoDomainModel>() {
            @Override
            public AddProductShopInfoDomainModel call(ShopModel shopModel) {
                boolean isGoldMerchant = shopModel.info.shopIsGold == 1;
                boolean isFreeReturn = shopModel.info.isFreeReturns();
                return new AddProductShopInfoDomainModel(isGoldMerchant, isFreeReturn);
            }
        });
    }
}
