package com.tokopedia.shop.info.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.reputation.speed.SpeedReputation;
import com.tokopedia.shop.info.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.info.domain.interactor.GetSpeedReputationUseCase;
import com.tokopedia.shop.info.view.listener.ShopPageView;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by normansyahputa on 2/13/18.
 */

public class ShopPagePresenter extends BaseDaggerPresenter<ShopPageView> {
    GetShopInfoUseCase getShopInfoUseCase;

    UserSession userSession;

    GetSpeedReputationUseCase getSpeedReputationUseCase;

    private String shopInfo;

    @Inject
    public ShopPagePresenter(GetShopInfoUseCase getShopInfoUseCase, UserSession userSession) {
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.userSession = userSession;
    }

    public void setGetSpeedReputationUseCase(GetSpeedReputationUseCase getSpeedReputationUseCase) {
        this.getSpeedReputationUseCase = getSpeedReputationUseCase;
    }

    public void setShopInfo(String shopInfo) {
        this.shopInfo = shopInfo;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void fetchData(){
        getSpeedReputationUseCase.execute(RequestParams.EMPTY, new Subscriber<SpeedReputation>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(SpeedReputation speedReputation) {
                if(isViewAttached()){
                    getView().renderData(speedReputation);
                }
            }
        });

        getShopInfoUseCase.execute(GetShopInfoUseCase.createRequestParam(shopInfo), new Subscriber<ShopInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ShopInfo shopInfo) {
                if(isViewAttached()){
                    getView().renderData(shopInfo);
                }
            }
        });
    }
}
