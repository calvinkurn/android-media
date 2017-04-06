package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.data.exception.FailedToAddEtalaseException;
import com.tokopedia.seller.product.domain.interactor.AddNewEtalaseUseCase;
import com.tokopedia.seller.product.domain.interactor.FetchMyEtalaseUseCase;
import com.tokopedia.seller.product.domain.model.MyEtalaseDomainModel;
import com.tokopedia.seller.product.view.mapper.MyEtalaseDomainToView;

import java.util.List;

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

    public void fetchEtalaseData(String shopId) {
        checkViewAttached();
        getView().clearEtalaseList();
        getView().dismissListRetry();
        getView().showListLoading();
        fetchMyEtalaseUseCase.execute(RequestParams.EMPTY, new FetchEtalaseDataSubscriber());
    }

    @Override
    public void addNewEtalase(String newEtalaseName) {
        checkViewAttached();
        getView().showLoadingDialog();
        RequestParams requestParam = AddNewEtalaseUseCase.generateRequestParam(newEtalaseName);
        addNewEtalaseUseCase.execute(requestParam, new AddNewEtalaseSubscribe(newEtalaseName));
    }

    private class FetchEtalaseDataSubscriber extends Subscriber<List<MyEtalaseDomainModel>> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            checkViewAttached();
            getView().dismissListLoading();
            getView().showListRetry();

        }

        @Override
        public void onNext(List<MyEtalaseDomainModel> etalases) {
            checkViewAttached();
            getView().dismissListLoading();
            getView().renderEtalaseList(MyEtalaseDomainToView.map(etalases));
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
            checkViewAttached();
            getView().dismissLoadingDialog();
            if (e instanceof FailedToAddEtalaseException){
                getView().showError(e.getLocalizedMessage());
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
