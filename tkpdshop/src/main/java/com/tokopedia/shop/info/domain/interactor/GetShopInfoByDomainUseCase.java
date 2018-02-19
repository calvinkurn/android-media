package com.tokopedia.shop.info.domain.interactor;

import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.shop.info.domain.repository.ShopInfoRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by User on 9/8/2017.
 */

public class GetShopInfoByDomainUseCase extends UseCase<ShopInfo> {

    private static final String SHOP_DOMAIN = "SHOP_DOMAIN";

    private ShopInfoRepository shopInfoRepository;

    @Inject
    public GetShopInfoByDomainUseCase(ShopInfoRepository shopInfoRepository) {
        super();
        this.shopInfoRepository = shopInfoRepository;
    }

    @Override
    public Observable<ShopInfo> createObservable(RequestParams requestParams) {
        String shopDomain = requestParams.getString(SHOP_DOMAIN, null);
        return shopInfoRepository.getShopInfoByDomain(shopDomain);
    }

    public static RequestParams createRequestParam(String shopDomain) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_DOMAIN, shopDomain);
        return requestParams;
    }
}
