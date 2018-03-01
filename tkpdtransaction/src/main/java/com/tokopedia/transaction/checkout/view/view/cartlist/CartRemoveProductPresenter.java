package com.tokopedia.transaction.checkout.view.view.cartlist;

import com.google.gson.Gson;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.checkout.data.entity.request.RemoveCartRequest;
import com.tokopedia.transaction.checkout.data.entity.request.UpdateCartRequest;
import com.tokopedia.transaction.checkout.domain.datamodel.DeleteUpdateCartData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.DeleteCartData;
import com.tokopedia.transaction.checkout.domain.usecase.ICartListInteractor;
import com.tokopedia.transaction.checkout.view.base.CartMvpPresenter;

import java.util.ArrayList;
import java.util.List;

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
        getMvpView().showList(cartItemModels);
    }

    public void processDeleteCart(List<CartItemData> cartItemDataListForDelete, List<CartItemData> cartItemForUpdate, boolean addWishList) {
        List<Integer> ids = new ArrayList<>();

        for (CartItemData data : cartItemDataListForDelete) {
            ids.add(data.getOriginData().getCartId());
        }

        RemoveCartRequest removeCartRequest = new RemoveCartRequest.Builder()
                .addWishlist(addWishList ? 1 : 0)
                .cartIds(ids)
                .build();

        TKPDMapParam<String, String> paramDelete = new TKPDMapParam<>();
        paramDelete.put("params", new Gson().toJson(removeCartRequest));

        List<UpdateCartRequest> updateCartRequestList = new ArrayList<>();

        for (CartItemData cartItemData : cartItemForUpdate) {
            updateCartRequestList.add(new UpdateCartRequest.Builder()
                            .cartId(cartItemData.getOriginData().getCartId())
                            .notes(cartItemData.getUpdatedData().getRemark())
                            .quantity(cartItemData.getUpdatedData().getQuantity())
                            .build()
            );
        }

        TKPDMapParam<String, String> paramUpdate = new TKPDMapParam<>();
        paramUpdate.put("carts", new Gson().toJson(updateCartRequestList));

        if (!updateCartRequestList.isEmpty()) {
            cartListInteractor.deleteAndUpdateCart(new Subscriber<DeleteUpdateCartData>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onNext(DeleteUpdateCartData deleteUpdateCartData) {
                    if (deleteUpdateCartData.isSuccess()) {
                        getMvpView().renderSuccessDeletePartialCart(deleteUpdateCartData.getMessage());
                    } else {
                        getMvpView().renderOnFailureDeleteCart(deleteUpdateCartData.getMessage());
                    }
                }
            }, getMvpView().getGenerateParamAuth(paramDelete), getMvpView().getGenerateParamAuth(paramUpdate));

        } else {
            cartListInteractor.deleteCart(new Subscriber<DeleteCartData>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onNext(DeleteCartData deleteCartData) {
                    if (deleteCartData.isSuccess()) {
                        getMvpView().renderSuccessDeleteAllCart(deleteCartData.getMessage());
                    } else {
                        getMvpView().renderOnFailureDeleteCart(deleteCartData.getMessage());
                    }
                }
            }, paramDelete);
        }
    }

}
