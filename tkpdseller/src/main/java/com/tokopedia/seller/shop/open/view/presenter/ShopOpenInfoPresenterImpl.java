package com.tokopedia.seller.shop.open.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.shop.open.domain.interactor.ShopIsReserveDomainUseCase;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.ResponseIsReserveDomain;
import com.tokopedia.seller.shop.open.domain.interactor.ShopOpenSaveInfoUseCase;
import com.tokopedia.seller.shop.open.domain.model.ShopOpenSaveInfoResponseModel;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 3/20/17.
 */

public class ShopOpenInfoPresenterImpl extends ShopOpenInfoPresenter {

    private final ShopOpenSaveInfoUseCase shopOpenSaveInfoUseCase;
    private final ShopIsReserveDomainUseCase shopIsReserveDomainUseCase;

    public ShopOpenInfoPresenterImpl(ShopOpenSaveInfoUseCase shopOpenSaveInfoUseCase, ShopIsReserveDomainUseCase shopIsReserveDomainUseCase) {
        this.shopOpenSaveInfoUseCase = shopOpenSaveInfoUseCase;
        this.shopIsReserveDomainUseCase = shopIsReserveDomainUseCase;
    }

    @Override
    public void submitShopInfo(String uriPathImage, String shopSlogan, String shopDescription, String imageUrl, String serverId, String picObj) {
        getView().showProgressDialog();
        shopOpenSaveInfoUseCase.execute(ShopOpenSaveInfoUseCase.createRequestParams(uriPathImage,
                shopDescription, shopSlogan, imageUrl, serverId, picObj), new Subscriber<ShopOpenSaveInfoResponseModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().dismissProgressDialog();
                    getView().onFailedSaveInfoShop(e);
                }
            }

            @Override
            public void onNext(ShopOpenSaveInfoResponseModel responseModel) {
                getView().dismissProgressDialog();
                if (responseModel.isSaveShopSuccess()) {
                    getView().onSuccessSaveInfoShop(responseModel);
                } else {
                    onError(new Exception());
                }
            }
        });
    }

    @Override
    public void getisReserveDomain() {
        getView().showProgressDialog();
        shopIsReserveDomainUseCase.execute(RequestParams.EMPTY, new Subscriber<ResponseIsReserveDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().dismissProgressDialog();
                    getView().onErrorGetReserveDomain(e);
                }
            }

            @Override
            public void onNext(ResponseIsReserveDomain responseIsReserveDomain) {
                getView().dismissProgressDialog();
                getView().onSuccessGetReserveDomain(responseIsReserveDomain);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        shopIsReserveDomainUseCase.unsubscribe();
        shopOpenSaveInfoUseCase.unsubscribe();
    }
}
