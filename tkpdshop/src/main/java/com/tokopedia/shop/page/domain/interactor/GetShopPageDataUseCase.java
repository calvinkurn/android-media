package com.tokopedia.shop.page.domain.interactor;

import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeed;
import com.tokopedia.reputation.common.domain.interactor.GetReputationSpeedUseCase;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.page.view.model.ShopPageViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by zulfikarrahman on 7/7/17.
 */

public class GetShopPageDataUseCase extends UseCase<ShopPageViewModel> {

    private static final String SHOP_ID = "SHOP_ID";

    private final GetShopInfoUseCase getShopInfoUseCase;
    private final GetReputationSpeedUseCase getReputationSpeedUseCase;

    public GetShopPageDataUseCase(GetShopInfoUseCase getShopInfoUseCase,
                                  GetReputationSpeedUseCase getReputationSpeedUseCase) {
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.getReputationSpeedUseCase = getReputationSpeedUseCase;
    }

    @Override
    public Observable<ShopPageViewModel> createObservable(RequestParams requestParams) {
        String shopId = requestParams.getString(SHOP_ID, "");
        return Observable.zip(getShopInfoUseCase.createObservable(GetShopInfoUseCase.createRequestParam(shopId)).subscribeOn(Schedulers.io()),
                getReputationSpeedUseCase.createObservable(GetReputationSpeedUseCase.createRequestParam(shopId)).subscribeOn(Schedulers.io()),
                new Func2<ShopInfo, ReputationSpeed, ShopPageViewModel>() {
                    @Override
                    public ShopPageViewModel call(ShopInfo shopInfo, ReputationSpeed reputationSpeed) {
                        ShopPageViewModel shopPageViewModel = new ShopPageViewModel();
                        shopPageViewModel.setShopInfo(shopInfo);
                        shopPageViewModel.setReputationSpeed(reputationSpeed);
                        return shopPageViewModel;
                    }
                });
    }

    public static RequestParams createRequestParam(String shopId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_ID, shopId);
        return requestParams;
    }
}