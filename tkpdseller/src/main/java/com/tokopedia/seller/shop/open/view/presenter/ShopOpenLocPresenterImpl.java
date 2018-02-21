package com.tokopedia.seller.shop.open.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Token;
import com.tokopedia.seller.logistic.GetOpenShopLocationPassUseCase;
import com.tokopedia.seller.logistic.GetOpenShopTokenUseCase;
import com.tokopedia.seller.shop.open.domain.interactor.ShopOpenSaveLocationUseCase;
import com.tokopedia.seller.shop.open.view.listener.ShopOpenLocView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by normansyahputa on 1/2/18.
 */

public class ShopOpenLocPresenterImpl extends BaseDaggerPresenter<ShopOpenLocView> {

    ShopOpenSaveLocationUseCase shopOpenSaveLocationUseCase;
    GetOpenShopTokenUseCase getOpenShopTokenUseCase;
    GetOpenShopLocationPassUseCase getOpenShopLocationPassUseCase;
    private boolean isHitToken;

    @Inject
    public ShopOpenLocPresenterImpl(ShopOpenSaveLocationUseCase shopOpenSaveLocationUseCase,
                                    GetOpenShopTokenUseCase getOpenShopTokenUseCase,
                                    GetOpenShopLocationPassUseCase getOpenShopLocationPassUseCase) {
        this.shopOpenSaveLocationUseCase = shopOpenSaveLocationUseCase;
        this.getOpenShopTokenUseCase = getOpenShopTokenUseCase;
        this.getOpenShopLocationPassUseCase = getOpenShopLocationPassUseCase;
    }

    public void submitData(RequestParams requestParams) {
        if(!isViewAttached())
            return;

        getView().showProgressDialog();

        shopOpenSaveLocationUseCase.execute(requestParams, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().dismissProgressDialog();
                    getView().updateStepperModel();
                    getView().onFailedSaveInfoShop(e);
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                getView().dismissProgressDialog();
                if (aBoolean == null || !isViewAttached())
                    return;
                getView().updateStepperModel();
                getView().goToNextPage(null);
            }
        });
    }

    public void openGoogleMap(RequestParams requestParams, final String generatedMap) {
        if (isHitToken)
            return;

        isHitToken = true;
        if (isViewAttached()) {
            getView().showProgressDialog();
        }
        getOpenShopLocationPassUseCase.execute(requestParams, new Subscriber<LocationPass>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                isHitToken = false;
                if (isViewAttached()) {
                    getView().dismissProgressDialog();
                    getView().onErrorGetReserveDomain(e);
                }
            }

            @Override
            public void onNext(LocationPass locationPass) {
                isHitToken = false;

                if (!isViewAttached())
                    return;

                if (isViewAttached()) {
                    getView().dismissProgressDialog();
                }

                getView().navigateToGoogleMap(generatedMap, locationPass);
            }
        });
    }

    public void openDistrictRecommendation(RequestParams requestParams) {
        if (isHitToken) {
            return;
        }
        isHitToken = true;
        if (isViewAttached()) {
            getView().showProgressDialog();
        }
        getOpenShopTokenUseCase.execute(requestParams, new Subscriber<Token>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                isHitToken = false;
                if (isViewAttached()) {
                    getView().dismissProgressDialog();
                    getView().onErrorGetReserveDomain(e);
                }
            }

            @Override
            public void onNext(Token token) {
                isHitToken = false;
                getView().dismissProgressDialog();
                getView().navigateToDistrictRecommendation(token);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        shopOpenSaveLocationUseCase.unsubscribe();
        getOpenShopTokenUseCase.unsubscribe();
        getOpenShopLocationPassUseCase.unsubscribe();
    }
}
