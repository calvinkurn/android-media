package com.tokopedia.transaction.checkout.view.presenter;

import com.google.gson.Gson;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.checkout.domain.ICartListInteractor;
import com.tokopedia.transaction.checkout.domain.request.RemoveCartRequest;
import com.tokopedia.transaction.checkout.view.data.CartItemData;
import com.tokopedia.transaction.checkout.view.data.DeleteCartData;
import com.tokopedia.transaction.checkout.view.view.IRemoveProductListView;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscriber;

/**
 * @author Aghny A. Putra on 05/02/18
 */

public class CartRemoveProductPresenter
        extends CartMvpPresenter<IRemoveProductListView<List<CartItemData>>> {

    private final ICartListInteractor cartListInteractor;

    public CartRemoveProductPresenter(ICartListInteractor cartListInteractor) {
        this.cartListInteractor = cartListInteractor;
    }

    @Override
    public void attachView(IRemoveProductListView<List<CartItemData>> mvpView) {
        super.attachView(mvpView);
    }

    @Override
    protected void checkViewAttached() {
        super.checkViewAttached();
    }

    public void getCartItems(List<CartItemData> cartItemModels) {
        // TODO remove this, and invoke use case
        getMvpView().showList(cartItemModels);
    }

    public void processDeleteCart(boolean addWishList) {
        List<CartItemData> cartItemDataListToDelete = getMvpView().getSelectedCartList();
        List<Integer> ids = new ArrayList<>();
        for (CartItemData data : cartItemDataListToDelete) {
            ids.add(data.getOriginData().getCartId());
        }
        RemoveCartRequest removeCartRequest = new RemoveCartRequest.Builder()
                .addWishlist(addWishList ? 1 : 0)
                .cartIds(ids)
                .build();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("params", new Gson().toJson(removeCartRequest));
        cartListInteractor.deleteCart(new Subscriber<DeleteCartData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(DeleteCartData deleteCartData) {
                if(deleteCartData.isSuccess())
                    getMvpView().renderSuccessDeleteCart(deleteCartData.getMessage());
            }
        }, getMvpView().getGenerateParamAuth(param));
    }

    private final class CartRemoveProductObserver implements Observer<List<CartItemData>> {

        @Override
        public void onNext(List<CartItemData> cartItemModels) {
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
