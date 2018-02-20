package com.tokopedia.transaction.checkout.view.presenter;

import android.content.Intent;

import com.google.gson.Gson;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.checkout.domain.ICartListInteractor;
import com.tokopedia.transaction.checkout.domain.request.RemoveCartRequest;
import com.tokopedia.transaction.checkout.view.activity.CartShipmentActivity;
import com.tokopedia.transaction.checkout.view.data.CartItemData;
import com.tokopedia.transaction.checkout.view.data.CartListData;
import com.tokopedia.transaction.checkout.view.data.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.view.data.DeleteCartData;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemHolderData;
import com.tokopedia.transaction.checkout.view.view.ICartListView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartListPresenter implements ICartListPresenter {
    private final ICartListView view;
    private final ICartListInteractor cartListInteractor;


    @Inject
    public CartListPresenter(ICartListView cartListView, ICartListInteractor cartListInteractor) {
        this.view = cartListView;
        this.cartListInteractor = cartListInteractor;
    }

    @Override
    public void processGetCartData() {
        view.disableSwipeRefresh();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("lang", "id");
        cartListInteractor.getCartList(new Subscriber<CartListData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(CartListData cartListData) {

                if (cartListData.getCartItemDataList().isEmpty()) {
                    view.renderEmptyCartData();
                } else {
                    view.renderPromoSuggestion(cartListData.getCartPromoSuggestion());
                    view.renderCartListData(cartListData.getCartItemDataList());
                }

            }
        }, view.getGeneratedAuthParamNetwork(param));
    }

    @Override
    public void processDeleteCart(final CartItemData cartItemData, boolean addWishList) {
        List<Integer> ids = new ArrayList<>();
        ids.add(cartItemData.getOriginData().getCartId());
        RemoveCartRequest removeCartRequest = new RemoveCartRequest.Builder()
                .cartIds(ids)
                .addWishlist(addWishList ? 1 : 0)
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
                view.renderSuccessDeleteCart(cartItemData, deleteCartData.getMessage());
            }
        }, view.getGeneratedAuthParamNetwork(param));
    }

    @Override
    public void processToShipmentStep() {
        List<CartItemData> cartItemDataList = extractCartItemList(view.getFinalCartList());
        CartPromoSuggestion cartPromoSuggestion = view.getCartPromoSuggestionData();
        Intent intent = CartShipmentActivity.createInstanceSingleAddress(
                view.getActivityContext(), cartItemDataList, cartPromoSuggestion
        );
        view.navigateToActivity(intent);

    }

    @Override
    public void reCalculateSubTotal(List<CartItemHolderData> dataList) {
        Locale LOCALE_ID = new Locale("in", "ID");
        NumberFormat CURRENCY_IDR = NumberFormat.getCurrencyInstance(LOCALE_ID);

        double subtotalPrice = 0;
        int qty = 0;
        for (CartItemHolderData data : dataList) {
            qty = qty + data.getCartItemData().getUpdatedData().getQuantity();
            subtotalPrice = subtotalPrice + (data.getCartItemData().getUpdatedData().getQuantity() * data.getCartItemData().getOriginData().getPricePlan());
        }

        view.renderDetailInfoSubTotal(String.valueOf(qty), CURRENCY_IDR.format(((int) subtotalPrice)));
    }

    private List<CartItemData> extractCartItemList(List<CartItemHolderData> finalCartList) {
        List<CartItemData> cartItemDataList = new ArrayList<>();
        for (CartItemHolderData data : finalCartList) {
            cartItemDataList.add(data.getCartItemData());
        }
        return cartItemDataList;
    }
}
