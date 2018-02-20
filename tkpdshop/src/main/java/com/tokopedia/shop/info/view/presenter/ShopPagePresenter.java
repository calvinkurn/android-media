package com.tokopedia.shop.info.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeed;
import com.tokopedia.reputation.common.domain.interactor.GetReputationSpeedUseCase;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.info.view.listener.ShopPageView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by normansyahputa on 2/13/18.
 */

public class ShopPagePresenter extends BaseDaggerPresenter<ShopPageView> {

    private final GetShopInfoUseCase getShopInfoUseCase;
    private final GetReputationSpeedUseCase getReputationSpeedUseCase;
    private final UserSession userSession;

    @Inject
    public ShopPagePresenter(GetShopInfoUseCase getShopInfoUseCase, GetReputationSpeedUseCase getReputationSpeedUseCase, UserSession userSession) {
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.getReputationSpeedUseCase = getReputationSpeedUseCase;
        this.userSession = userSession;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void getShopInfo(String shopId) {
        getShopInfoUseCase.execute(GetShopInfoUseCase.createRequestParam(shopId), new Subscriber<ShopInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorGetShopInfo(e);
                }
            }

            @Override
            public void onNext(ShopInfo shopInfo) {
                getView().onSuccessGetShopInfo(shopInfo);
                getReputationSpeed(shopInfo.getInfo().getShopId());
            }
        });
    }

    private void getReputationSpeed(String shopId) {
        getReputationSpeedUseCase.execute(GetReputationSpeedUseCase.createRequestParam(shopId), new Subscriber<ReputationSpeed>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorGetReputationSpeed(e);
                }
            }

            @Override
            public void onNext(ReputationSpeed reputationSpeed) {
                getView().onSuccessGetReputationSpeed(reputationSpeed);
            }
        });


    }
}
