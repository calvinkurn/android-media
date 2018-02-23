package com.tokopedia.shop.page.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeed;
import com.tokopedia.reputation.common.domain.interactor.GetReputationSpeedUseCase;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.shop.page.domain.interactor.ToggleFavouriteShopAndDeleteCacheUseCase;
import com.tokopedia.shop.page.view.listener.ShopPageView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by normansyahputa on 2/13/18.
 */

public class ShopPagePresenter extends BaseDaggerPresenter<ShopPageView> {

    private final GetShopInfoUseCase getShopInfoUseCase;
    private final GetReputationSpeedUseCase getReputationSpeedUseCase;
    private final ToggleFavouriteShopAndDeleteCacheUseCase toggleFavouriteShopAndDeleteCacheUseCase;
    private final UserSession userSession;

    @Inject
    public ShopPagePresenter(GetShopInfoUseCase getShopInfoUseCase,
                             GetReputationSpeedUseCase getReputationSpeedUseCase,
                             ToggleFavouriteShopAndDeleteCacheUseCase toggleFavouriteShopAndDeleteCacheUseCase,
                             UserSession userSession) {
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.getReputationSpeedUseCase = getReputationSpeedUseCase;
        this.toggleFavouriteShopAndDeleteCacheUseCase = toggleFavouriteShopAndDeleteCacheUseCase;
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

    public void toggleFavouriteShop(String shopId) {
        toggleFavouriteShopAndDeleteCacheUseCase.execute(ToggleFavouriteShopUseCase.createRequestParam(shopId), new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorToggleFavourite(e);
                }
            }

            @Override
            public void onNext(Boolean success) {
                getView().onSuccessToggleFavourite(success);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        if (getShopInfoUseCase != null) {
            getShopInfoUseCase.unsubscribe();
        }
        if (getReputationSpeedUseCase != null) {
            getReputationSpeedUseCase.unsubscribe();
        }
        if (toggleFavouriteShopAndDeleteCacheUseCase != null) {
            toggleFavouriteShopAndDeleteCacheUseCase.unsubscribe();
        }
    }
}
