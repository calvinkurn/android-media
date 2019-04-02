package com.tokopedia.seller.product.etalase.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.exception.ResponseV4ErrorException;
import com.tokopedia.seller.product.etalase.domain.interactor.AddNewEtalaseUseCase;
import com.tokopedia.seller.product.etalase.domain.interactor.FetchMyEtalaseUseCase;
import com.tokopedia.seller.product.etalase.domain.model.MyEtalaseDomainModel;
import com.tokopedia.seller.product.etalase.view.mapper.MyEtalaseDomainToView;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class EtalasePickerPresenterImpl extends EtalasePickerPresenter {

    private final FetchMyEtalaseUseCase fetchMyEtalaseUseCase;
    private final AddNewEtalaseUseCase addNewEtalaseUseCase;

    public EtalasePickerPresenterImpl(FetchMyEtalaseUseCase fetchMyEtalaseUseCase,
                                      AddNewEtalaseUseCase addNewEtalaseUseCase) {
        this.fetchMyEtalaseUseCase = fetchMyEtalaseUseCase;
        this.addNewEtalaseUseCase = addNewEtalaseUseCase;
    }

    public void fetchFirstPageEtalaseData() {
        if (!isViewAttached()) {
            return;
        }
        getView().clearEtalaseList();
        getView().dismissListRetry();
        getView().showListLoading();
        fetchMyEtalaseUseCase.execute(RequestParams.EMPTY, new FetchEtalaseDataSubscriber());
    }

    @Override
    public void addNewEtalase(String newEtalaseName) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        RequestParams requestParam = AddNewEtalaseUseCase.generateRequestParam(newEtalaseName);
        addNewEtalaseUseCase.execute(requestParam, new AddNewEtalaseSubscribe(newEtalaseName));
    }

    @Override
    public void fetchNextPageEtalaseData(int page) {
        if (!isViewAttached()) {
            return;
        }
        getView().showNextListLoading();
        RequestParams requestParam = FetchMyEtalaseUseCase.generateRequestParam(page);
        fetchMyEtalaseUseCase.execute(requestParam, new FetchEtalaseNextPageDataSubscriber());
    }

    private class FetchEtalaseDataSubscriber extends Subscriber<MyEtalaseDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (!isViewAttached()) {
                return;
            }
            getView().dismissListLoading();
            getView().showListRetry();
        }

        @Override
        public void onNext(MyEtalaseDomainModel etalases) {
            if (!isViewAttached()) {
                return;
            }
            getView().dismissListLoading();
            getView().renderEtalaseList(MyEtalaseDomainToView.map(etalases));
        }
    }

    private class FetchEtalaseNextPageDataSubscriber extends FetchEtalaseDataSubscriber {
        @Override
        public void onError(Throwable e) {
            if (!isViewAttached()) {
                return;
            }
            getView().dismissListLoading();
            getView().showError(e);
        }
    }

    private class AddNewEtalaseSubscribe extends Subscriber<Boolean> {
        private final String newEtalaseName;

        private AddNewEtalaseSubscribe(String newEtalaseName) {
            this.newEtalaseName = newEtalaseName;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (!isViewAttached()) {
                return;
            }
            getView().dismissLoadingDialog();
            if (e instanceof ResponseV4ErrorException){
                getView().showError(e);
            } else {
                getView().showRetryAddNewEtalase(newEtalaseName);
            }
        }

        @Override
        public void onNext(Boolean aBoolean) {
            checkViewAttached();
            getView().dismissLoadingDialog();
            getView().refreshEtalaseData();
        }
    }
}
