package com.tokopedia.seller.logistic;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.district_recommendation.domain.model.Token;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.seller.shopsettings.shipping.interactor.EditShippingInteractorImpl;
import com.tokopedia.seller.shopsettings.shipping.model.openshopshipping.OpenShopData;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 12/29/17.
 */

public class GetOpenShopLocationPassUseCase extends UseCase<LocationPass> {

    private final EditShippingInteractorImpl editShippingInteractor;
    private OpenShopData model = null;

    public static final String GENERATED_ADDRESS = "GENERATED_ADDRESS";

    public GetOpenShopLocationPassUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        editShippingInteractor = new EditShippingInteractorImpl();
    }

    @Override
    public Observable<LocationPass> createObservable(final RequestParams requestParams) {
        if(model != null){
            return Observable.just(createLocationPass(model, requestParams.getString(GENERATED_ADDRESS, "")));
        }

        return editShippingInteractor.getOpenShopData(requestParams.getParamsAllValueInString())
                .doOnNext(new Action1<Response<TkpdResponse>>() {
                    @Override
                    public void call(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            model = response.body().convertDataObj(OpenShopData.class);
                        }else {
                            new ErrorHandler(new ErrorListener() {
                                @Override
                                public void onUnknown() {
                                    throw new RuntimeException();
                                }

                                @Override
                                public void onTimeout() {
                                    throw new RuntimeException();
                                }

                                @Override
                                public void onServerError() {
                                    throw new RuntimeException();
                                }

                                @Override
                                public void onBadRequest() {
                                    throw new RuntimeException();
                                }

                                @Override
                                public void onForbidden() {
                                    throw new RuntimeException();
                                }
                            }, response.code());
                        }
                    }
                })
                .map(new Func1<Response<TkpdResponse>, LocationPass>() {
                    @Override
                    public LocationPass call(Response<TkpdResponse> tkpdResponseResponse) {
                        return createLocationPass(model, requestParams.getString(GENERATED_ADDRESS, ""));
                    }
                });
    }

    private LocationPass createLocationPass(OpenShopData openShopData, String generatedAddress){
        if (!model.getShopShipping().getShopLatitude().isEmpty()
                && !model.getShopShipping().getShopLongitude().isEmpty()) {
            LocationPass locationPass = new LocationPass();
            locationPass.setLatitude(model.getShopShipping().getShopLatitude());
            locationPass.setLongitude(model.getShopShipping().getShopLongitude());
            locationPass.setCityName(model.getShopShipping().getCityName());
            locationPass.setDistrictName(model.getShopShipping().getDistrictName());
            locationPass.setGeneratedAddress(generatedAddress);

            return locationPass;
        }

        return null;
    }
}
