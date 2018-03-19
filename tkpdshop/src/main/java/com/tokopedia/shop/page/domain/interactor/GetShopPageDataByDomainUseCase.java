package com.tokopedia.shop.page.domain.interactor;

import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeed;
import com.tokopedia.reputation.common.domain.interactor.GetReputationSpeedUseCase;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase;
import com.tokopedia.shop.page.view.model.ShopPageViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 7/7/17.
 */

public class GetShopPageDataByDomainUseCase extends UseCase<ShopPageViewModel> {

    private static final String SHOP_DOMAIN = "SHOP_DOMAIN";

    private final GetShopInfoByDomainUseCase getShopInfoByDomainUseCase;
    private final GetReputationSpeedUseCase getReputationSpeedUseCase;

    public GetShopPageDataByDomainUseCase(GetShopInfoByDomainUseCase getShopInfoByDomainUseCase,
                                          GetReputationSpeedUseCase getReputationSpeedUseCase) {
        this.getShopInfoByDomainUseCase = getShopInfoByDomainUseCase;
        this.getReputationSpeedUseCase = getReputationSpeedUseCase;
    }

    @Override
    public Observable<ShopPageViewModel> createObservable(RequestParams requestParams) {
        String shopDomain = requestParams.getString(SHOP_DOMAIN, "");
        return getShopInfoByDomainUseCase.createObservable(GetShopInfoByDomainUseCase.createRequestParam(shopDomain)).flatMap(new Func1<ShopInfo, Observable<ShopPageViewModel>>() {
            @Override
            public Observable<ShopPageViewModel> call(final ShopInfo shopInfo) {
                return getReputationSpeedUseCase.createObservable(GetReputationSpeedUseCase.createRequestParam(shopInfo.getInfo().getShopId())).flatMap(new Func1<ReputationSpeed, Observable<ShopPageViewModel>>() {
                    @Override
                    public Observable<ShopPageViewModel> call(ReputationSpeed reputationSpeed) {
                        ShopPageViewModel shopPageViewModel = new ShopPageViewModel();
                        shopPageViewModel.setShopInfo(shopInfo);
                        shopPageViewModel.setReputationSpeed(reputationSpeed);
                        return Observable.just(shopPageViewModel);
                    }
                });
            }
        });
    }

    public static RequestParams createRequestParam(String shopDomain) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_DOMAIN, shopDomain);
        return requestParams;
    }
}