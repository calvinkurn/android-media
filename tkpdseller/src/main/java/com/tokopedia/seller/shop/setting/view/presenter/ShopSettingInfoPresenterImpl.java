package com.tokopedia.seller.shop.setting.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.shop.open.domain.interactor.ShopIsReserveDomainUseCase;
import com.tokopedia.seller.shop.open.data.model.response.ResponseIsReserveDomain;
import com.tokopedia.seller.shop.setting.domain.interactor.ShopSettingSaveInfoUseCase;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 3/20/17.
 */

public class ShopSettingInfoPresenterImpl extends ShopSettingInfoPresenter {

    private final ShopSettingSaveInfoUseCase shopSettingSaveInfoUseCase;
    private final ShopIsReserveDomainUseCase shopIsReserveDomainUseCase;

    public ShopSettingInfoPresenterImpl(ShopSettingSaveInfoUseCase shopSettingSaveInfoUseCase, ShopIsReserveDomainUseCase shopIsReserveDomainUseCase) {
        this.shopSettingSaveInfoUseCase = shopSettingSaveInfoUseCase;
        this.shopIsReserveDomainUseCase = shopIsReserveDomainUseCase;
    }

    @Override
    public void submitShopInfo(String uriPathImage, String shopSlogan, String shopDescription, String imageUrl, String serverId, String picObj) {
        getView().showProgressDialog();
        shopSettingSaveInfoUseCase.execute(ShopSettingSaveInfoUseCase.createRequestParams(uriPathImage,
                shopDescription, shopSlogan, imageUrl, serverId, picObj), new Subscriber<Boolean>() {
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
            public void onNext(Boolean isSuccess) {
                getView().dismissProgressDialog();
                if (isSuccess) {
                    getView().onSuccessSaveInfoShop();
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
        shopSettingSaveInfoUseCase.unsubscribe();
    }
}
