package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.seller.topads.data.model.data.Etalase;

import java.util.List;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class EtalasePickerPresenterImpl extends EtalasePickerPresenter {

    public EtalasePickerPresenterImpl() {

    }

    public void fetchEtalaseData(String shopId) {
        checkViewAttached();
        getView().showLoading();

    }

    private class FetchEtalaseDataSubscriber extends Subscriber<List<Etalase>> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            checkViewAttached();
            getView().dismissLoading();

        }

        @Override
        public void onNext(List<Etalase> etalases) {
            checkViewAttached();
            getView().renderEtalaseList(etalases);
        }
    }
}
