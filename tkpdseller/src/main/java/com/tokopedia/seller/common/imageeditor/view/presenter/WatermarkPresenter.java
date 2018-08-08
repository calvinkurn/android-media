package com.tokopedia.seller.common.imageeditor.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.common.imageeditor.view.WatermarkPresenterView;
import com.tokopedia.seller.shop.common.domain.interactor.GetShopInfoUseCase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by hendry on 10/20/2017.
 */

public class WatermarkPresenter extends BaseDaggerPresenter<WatermarkPresenterView> {
    private GetShopInfoUseCase getShopInfoUseCase;

    @Inject
    public WatermarkPresenter(GetShopInfoUseCase getShopInfoUseCase) {
        this.getShopInfoUseCase = getShopInfoUseCase;
    }

    public void getShopInfo(){
        getShopInfoUseCase.execute(null, getShopInfoSubscriber());
    }

    private Subscriber<ShopModel> getShopInfoSubscriber() {
        return new Subscriber<ShopModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onErrorGetShopInfo(e);
            }

            @Override
            public void onNext(ShopModel shopModel) {
                getView().onSuccessGetShopInfo(shopModel);
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopInfoUseCase.unsubscribe();
    }
}
