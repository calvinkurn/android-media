package com.tokopedia.seller.shop.open.view.presenter;

import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Token;
import com.tokopedia.seller.logistic.GetOpenShopLocationPassUseCase;
import com.tokopedia.seller.logistic.GetOpenShopTokenUseCase;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenMandatoryLocationFragment;
import com.tokopedia.seller.shop.setting.domain.interactor.ShopOpenSaveLocationUseCase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by normansyahputa on 1/2/18.
 */

public class ShopOpenLocPresenterImpl extends BaseDaggerPresenter<ShopOpenLocView> {

    private static final String TAG = "ShopOpenLocPresenterImp";

    ShopOpenSaveLocationUseCase shopOpenSaveLocationUseCase;

    GetOpenShopTokenUseCase getOpenShopTokenUseCase;

    GetOpenShopLocationPassUseCase getOpenShopLocationPassUseCase;

    @Inject
    public ShopOpenLocPresenterImpl(ShopOpenSaveLocationUseCase shopOpenSaveLocationUseCase,
                                    GetOpenShopTokenUseCase getOpenShopTokenUseCase,
                                    GetOpenShopLocationPassUseCase getOpenShopLocationPassUseCase) {
        this.shopOpenSaveLocationUseCase = shopOpenSaveLocationUseCase;
        this.getOpenShopTokenUseCase = getOpenShopTokenUseCase;
        this.getOpenShopLocationPassUseCase = getOpenShopLocationPassUseCase;
    }

    public void submitData(RequestParams requestParams){
        shopOpenSaveLocationUseCase.execute(requestParams, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "berhasilkah ? -> "+e);
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if(aBoolean == null || !isViewAttached())
                    return;

                getView().updateStepperModel();
                getView().goToNextPage(null);
            }
        });
    }

    public void openGoogleMap(RequestParams requestParams, final String generatedMap){
        getOpenShopLocationPassUseCase.execute(requestParams, new Subscriber<LocationPass>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(LocationPass locationPass) {
                if(!isViewAttached())
                    return;

                getView().navigateToGoogleMap(generatedMap, locationPass);
            }
        });
    }

    public void openDistrictRecommendation((RequestParams requestParams){
        getOpenShopTokenUseCase.execute(requestParams, new Subscriber<Token>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Token token) {
                if(!isViewAttached())
                    return;

                getView().navigateToDistrictRecommendation(token);
            }
        });
    }
}
