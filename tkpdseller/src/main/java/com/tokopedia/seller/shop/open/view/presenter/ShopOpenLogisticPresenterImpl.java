package com.tokopedia.seller.shop.open.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.logistic.model.CouriersModel;
import com.tokopedia.seller.shop.open.data.model.response.ResponseCreateShop;
import com.tokopedia.seller.shop.open.view.model.CourierServiceIdWrapper;
import com.tokopedia.seller.logistic.domain.interactor.GetLogisticAvailableUseCase;
import com.tokopedia.seller.shop.open.domain.interactor.ShopOpenCreateUseCase;
import com.tokopedia.seller.shop.open.domain.interactor.ShopOpenSaveCourierUseCase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by sebastianuskh on 3/23/17.
 */

public class ShopOpenLogisticPresenterImpl extends ShopOpenLogisticPresenter {

    public static final String SUCCESS = "1";
    private final GetLogisticAvailableUseCase getLogisticAvailableUseCase;
    private final ShopOpenSaveCourierUseCase shopOpenSaveCourierUseCase;
    private final ShopOpenCreateUseCase shopOpenCreateUseCase;

    @Inject
    public ShopOpenLogisticPresenterImpl(GetLogisticAvailableUseCase getLogisticAvailableUseCase,
                                         ShopOpenSaveCourierUseCase shopOpenSaveCourierUseCase,
                                         ShopOpenCreateUseCase shopOpenCreateUseCase) {
        this.getLogisticAvailableUseCase = getLogisticAvailableUseCase;
        this.shopOpenSaveCourierUseCase = shopOpenSaveCourierUseCase;
        this.shopOpenCreateUseCase = shopOpenCreateUseCase;
    }

    @Override
    public void getCouriers(int districtCode) {
        RequestParams requestParam = GetLogisticAvailableUseCase.generateParams(districtCode);
        getLogisticAvailableUseCase.execute(requestParam, new Subscriber<CouriersModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorLoadLogistic(e);
                }
            }

            @Override
            public void onNext(CouriersModel couriersModel) {
                getView().onSuccessLoadLogistic(couriersModel);
            }
        });
    }

    @Override
    public void saveCourier(CourierServiceIdWrapper courierServiceIdWrapper) {
        RequestParams requestParam = ShopOpenSaveCourierUseCase.createRequestParams(courierServiceIdWrapper);
        shopOpenSaveCourierUseCase.execute(requestParam, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorSaveCourier(e);
                }
            }

            @Override
            public void onNext(Boolean success) {
                if (success) {
                    createShop();
                } else {
                    getView().onErrorSaveCourier(null);
                }
            }
        });
    }

    private void createShop() {
        shopOpenCreateUseCase.execute(null, new Subscriber<ResponseCreateShop>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorCreateShop(e);
                }
            }

            @Override
            public void onNext(ResponseCreateShop responseCreateShop) {
                if (responseCreateShop.getReserveStatus().equals(SUCCESS)) {
                    getView().onSuccessCreateShop(responseCreateShop.getShopId());
                } else {
                    getView().onErrorCreateShop(new Exception());
                }
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        getLogisticAvailableUseCase.unsubscribe();
        shopOpenSaveCourierUseCase.unsubscribe();
        shopOpenCreateUseCase.unsubscribe();
    }
}