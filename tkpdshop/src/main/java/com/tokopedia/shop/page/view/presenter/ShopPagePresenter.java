package com.tokopedia.shop.page.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.shop.page.domain.interactor.GetShopPageDataByDomainUseCase;
import com.tokopedia.shop.page.domain.interactor.GetShopPageDataUseCase;
import com.tokopedia.shop.page.domain.interactor.ToggleFavouriteShopAndDeleteCacheUseCase;
import com.tokopedia.shop.page.view.listener.ShopPageView;
import com.tokopedia.shop.page.view.model.ShopPageViewModel;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by normansyahputa on 2/13/18.
 */

public class ShopPagePresenter extends BaseDaggerPresenter<ShopPageView> {

    private final GetShopPageDataUseCase getShopPageDataUseCase;
    private final GetShopPageDataByDomainUseCase getShopPageDataByDomainUseCase;
    private final ToggleFavouriteShopAndDeleteCacheUseCase toggleFavouriteShopAndDeleteCacheUseCase;
    private final UserSession userSession;

    @Inject
    public ShopPagePresenter(GetShopPageDataUseCase getShopPageDataUseCase,
                             GetShopPageDataByDomainUseCase getShopInfoByDomainUseCase,
                             ToggleFavouriteShopAndDeleteCacheUseCase toggleFavouriteShopAndDeleteCacheUseCase,
                             UserSession userSession) {
        this.getShopPageDataUseCase = getShopPageDataUseCase;
        this.getShopPageDataByDomainUseCase = getShopInfoByDomainUseCase;
        this.toggleFavouriteShopAndDeleteCacheUseCase = toggleFavouriteShopAndDeleteCacheUseCase;
        this.userSession = userSession;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void getShopInfo(String shopId) {
        getShopPageDataUseCase.execute(GetShopInfoUseCase.createRequestParam(shopId), new Subscriber<ShopPageViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorGetShopPageInfo(e);
                }
            }

            @Override
            public void onNext(ShopPageViewModel shopPageViewModel) {
                getView().onSuccessGetShopPageInfo(shopPageViewModel);
            }
        });
    }

    public void getShopInfoByDomain(String shopDomain) {
        getShopPageDataByDomainUseCase.execute(GetShopPageDataByDomainUseCase.createRequestParam(shopDomain), new Subscriber<ShopPageViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorGetShopPageInfo(e);
                }
            }

            @Override
            public void onNext(ShopPageViewModel shopPageViewModel) {
                getView().onSuccessGetShopPageInfo(shopPageViewModel);
            }
        });
    }

    public void toggleFavouriteShop(String shopId) {
        if (!userSession.isLoggedIn()) {
            if (isViewAttached()) {
                getView().onErrorToggleFavourite(new UserNotLoginException());
            }
            return;
        }
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
        if (getShopPageDataUseCase != null) {
            getShopPageDataUseCase.unsubscribe();
        }
        if (getShopPageDataByDomainUseCase != null) {
            getShopPageDataByDomainUseCase.unsubscribe();
        }
        if (toggleFavouriteShopAndDeleteCacheUseCase != null) {
            toggleFavouriteShopAndDeleteCacheUseCase.unsubscribe();
        }
    }
}
