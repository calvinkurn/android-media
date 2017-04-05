package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
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

    public EtalasePickerPresenterImpl(FetchMyEtalaseUseCase fetchMyEtalaseUseCase) {
        this.fetchMyEtalaseUseCase = fetchMyEtalaseUseCase;
    }

    public void fetchEtalaseData(String shopId) {
        checkViewAttached();
        getView().showLoading();
        fetchMyEtalaseUseCase.execute(RequestParams.EMPTY, new FetchEtalaseDataSubscriber());
    }

    private class FetchEtalaseDataSubscriber extends Subscriber<List<MyEtalaseDomainModel>> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            checkViewAttached();
            getView().dismissLoading();

        }

        @Override
        public void onNext(List<MyEtalaseDomainModel> etalases) {
            checkViewAttached();
            getView().dismissLoading();
            getView().renderEtalaseList(MyEtalaseDomainToView.map(etalases));
        }
    }
}
