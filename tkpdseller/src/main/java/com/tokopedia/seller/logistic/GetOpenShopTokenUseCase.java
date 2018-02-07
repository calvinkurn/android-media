package com.tokopedia.seller.logistic;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Token;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.seller.shopsettings.shipping.interactor.EditShippingInteractorImpl;
import com.tokopedia.seller.shopsettings.shipping.model.openshopshipping.OpenShopData;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 12/29/17.
 */

public class GetOpenShopTokenUseCase extends UseCase<Token> {
    private final EditShippingInteractorImpl editShippingInteractor;
    private OpenShopData model = null;

    public GetOpenShopTokenUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        editShippingInteractor = new EditShippingInteractorImpl();
    }

    @Override
    public Observable<Token> createObservable(RequestParams requestParams) {

        if(model != null){
            return Observable.just(model.getToken());
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
                .map(new Func1<Response<TkpdResponse>, Token>() {
                    @Override
                    public Token call(Response<TkpdResponse> tkpdResponseResponse) {
                        return model.getToken();
                    }
                });
    }
}
