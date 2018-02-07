package com.tokopedia.transaction.insurance.view;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.transaction.insurance.domain.InsuranceTnCUseCase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 11/12/17.
 */

public class InsuranceTnCPresenter extends BaseDaggerPresenter<InsuranceTnCContract.View>
        implements InsuranceTnCContract.Presenter {

    private InsuranceTnCUseCase insuranceTnCUseCase;

    @Inject
    InsuranceTnCContract.Presenter presenter;

    @Inject
    public InsuranceTnCPresenter(InsuranceTnCUseCase insuranceTnCUseCase) {
        this.insuranceTnCUseCase = insuranceTnCUseCase;
    }

    @Override
    public void attachView(InsuranceTnCContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        insuranceTnCUseCase.unsubscribe();
    }

    @Override
    public void loadWebViewData() {
        getView().showLoading();
        insuranceTnCUseCase.execute(RequestParams.create(), new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideLoading();
                }
            }

            @Override
            public void onNext(String webViewData) {
                if (isViewAttached()) {
//                    getView().hideLoading();
                    getView().showWebView(webViewData);
                }
            }
        });
    }

}
