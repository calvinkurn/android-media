package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.transaction.checkout.view.data.SingleAddressRecipientAddressModel;
import com.tokopedia.transaction.checkout.view.view.ISearchAddressListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observer;

public class CartAddressListPresenter
        extends BaseListPresenter<ISearchAddressListView<List<SingleAddressRecipientAddressModel>>> {

    @Inject
    CartAddressListPresenter() {

    }

    @Override
    public void attachView(ISearchAddressListView<List<SingleAddressRecipientAddressModel>> mvpView) {
        super.attachView(mvpView);
    }

    @Override
    protected void checkViewAttached() {
        super.checkViewAttached();
    }

    public List<SingleAddressRecipientAddressModel> getAddressList() {
        List<SingleAddressRecipientAddressModel> list = new ArrayList<>();
        return list;
    }

    private final class AddressListObserver implements Observer<List<SingleAddressRecipientAddressModel>> {

        @Override
        public void onNext(List<SingleAddressRecipientAddressModel> addressList) {
            if (addressList.isEmpty()) {
                getMvpView().showListEmpty();
            } else {
                getMvpView().showList(addressList);
            }
        }

        @Override
        public void onError(Throwable e) {
            getMvpView().showError();
        }

        @Override
        public void onCompleted() {

        }

    }

}
