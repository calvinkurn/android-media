package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.transaction.checkout.view.data.CartItemModel;
import com.tokopedia.transaction.checkout.view.data.factory.CartItemModelFactory;
import com.tokopedia.transaction.checkout.view.view.IRemoveProductListView;

import java.util.List;

import rx.Observer;

/**
 * @author Aghny A. Putra on 05/02/18
 */

public class CartRemoveProductPresenter
        extends CartMvpPresenter<IRemoveProductListView<List<CartItemModel>>> {

    public CartRemoveProductPresenter() {

    }

    @Override
    public void attachView(IRemoveProductListView<List<CartItemModel>> mvpView) {
        super.attachView(mvpView);
    }

    @Override
    protected void checkViewAttached() {
        super.checkViewAttached();
    }

    public void getCartItems() {
        // TODO remove this, and invoke use case
        getMvpView().showList(CartItemModelFactory.getDummyCartItemModelList());
    }

    private final class CartRemoveProductObserver implements Observer<List<CartItemModel>> {

        @Override
        public void onNext(List<CartItemModel> cartItemModels) {
            getMvpView().showList(cartItemModels);
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
