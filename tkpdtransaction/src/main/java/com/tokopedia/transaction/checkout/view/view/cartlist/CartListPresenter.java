package com.tokopedia.transaction.checkout.view.view.cartlist;

import android.content.Intent;

import com.google.gson.Gson;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.checkout.domain.ICartListInteractor;
import com.tokopedia.transaction.checkout.domain.request.RemoveCartRequest;
import com.tokopedia.transaction.checkout.domain.request.UpdateCartRequest;
import com.tokopedia.transaction.checkout.view.data.CartItemData;
import com.tokopedia.transaction.checkout.view.data.CartListData;
import com.tokopedia.transaction.checkout.view.data.DeleteCartData;
import com.tokopedia.transaction.checkout.view.data.UpdateToSingleAddressShipmentData;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemHolderData;
import com.tokopedia.transaction.checkout.view.view.shipmentform.CartShipmentActivity;

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
        view.renderLoadGetCartData();
        view.disableSwipeRefresh();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("lang", "id");
        cartListInteractor.getCartList(new Subscriber<CartListData>() {
            @Override
            public void onCompleted() {
                view.renderLoadGetCartDataFinish();
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
                    view.renderPromoVoucher();
                    view.renderPromoSuggestion(cartListData.getCartPromoSuggestion());
                    view.renderCartListData(cartListData.getCartItemDataList());
                }

            }
        }, view.getGeneratedAuthParamNetwork(param));
    }

    @Override
    public void processDeleteCart(final CartItemData cartItemData, final boolean addWishList) {
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
                if (deleteCartData.isSuccess())
                    view.renderSuccessDeleteCart(cartItemData, deleteCartData.getMessage(), addWishList);
            }
        }, view.getGeneratedAuthParamNetwork(param));
    }

    @Override
    public void processToShipmentStep() {
        List<CartItemData> cartItemDataList = extractCartItemList(view.getFinalCartList());
        List<UpdateCartRequest> updateCartRequestList = new ArrayList<>();
        for (CartItemData data : cartItemDataList) {
            updateCartRequestList.add(new UpdateCartRequest.Builder()
                    .cartId(data.getOriginData().getCartId())
                    .notes(data.getUpdatedData().getRemark())
                    .quantity(data.getUpdatedData().getQuantity())
                    .build());
        }
        TKPDMapParam<String, String> paramUpdate = new TKPDMapParam<>();
        paramUpdate.put("carts", new Gson().toJson(updateCartRequestList));

        TKPDMapParam<String, String> paramGetShipmentForm = new TKPDMapParam<>();
        paramGetShipmentForm.put("lang", "id");

        cartListInteractor.updateCartToSingleAddressShipment(
                new Subscriber<UpdateToSingleAddressShipmentData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UpdateToSingleAddressShipmentData updateCartListData) {
                        if (updateCartListData.getUpdateCartData().isSuccess()) {
                            if (updateCartListData.getShipmentAddressFormData().isMultiple()) {
                                Intent intent = CartShipmentActivity.createInstanceMultipleAddress(
                                        view.getActivityContext(),
                                        updateCartListData.getShipmentAddressFormData(),
                                        view.getCartPromoSuggestion()
                                );
                                view.navigateToActivityRequest(intent,
                                        CartShipmentActivity.REQUEST_CODE);
                            } else {
                                Intent intent = CartShipmentActivity.createInstanceSingleAddress(
                                        view.getActivityContext(),
                                        updateCartListData.getShipmentAddressFormData(),
                                        view.getCartPromoSuggestion()
                                );
                                view.navigateToActivityRequest(intent,
                                        CartShipmentActivity.REQUEST_CODE);
                            }

                        } else {
                            view.renderUpdateDataFailed(
                                    updateCartListData.getUpdateCartData().getMessage()
                            );
                        }
                    }
                },
                view.getGeneratedAuthParamNetwork(paramUpdate),
                view.getGeneratedAuthParamNetwork(paramGetShipmentForm)
        );

    }

    @Override
    public void processToShipmentMultipleAddress() {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("lang", "id");
        param.put("isReset", "false");
        cartListInteractor.getCartList(new Subscriber<CartListData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(CartListData cartListData) {
                view.renderToShipmentMultipleAddressSuccess(cartListData);
            }
        }, view.getGeneratedAuthParamNetwork(param));
    }

    @Override
    public void reCalculateSubTotal(List<CartItemHolderData> dataList) {
        Locale LOCALE_ID = new Locale("in", "ID");
        NumberFormat CURRENCY_IDR = NumberFormat.getCurrencyInstance(LOCALE_ID);

        double subtotalPrice = 0;
        int qty = 0;
        for (CartItemHolderData data : dataList) {
            qty = qty + data.getCartItemData().getUpdatedData().getQuantity();
            subtotalPrice = subtotalPrice
                    + (data.getCartItemData().getUpdatedData().getQuantity()
                    * data.getCartItemData().getOriginData().getPricePlan());
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
