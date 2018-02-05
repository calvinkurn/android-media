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

public class SaveShopIdUseCase extends UseCase<ShopInfo> {

    private ShopInfoRepository shopInfoRepository;

    @Inject
    public SaveShopIdUseCase(ShopInfoRepository shopInfoRepository) {
        super();
        this.shopInfoRepository = shopInfoRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return shopInfoRepository.getShopInfo();
    }
}
