package com.tokopedia.transaction.checkout.view.view.cartlist;

import com.google.gson.Gson;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ResponseErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.checkout.data.entity.request.RemoveCartRequest;
import com.tokopedia.transaction.checkout.data.entity.request.UpdateCartRequest;
import com.tokopedia.transaction.checkout.data.exception.ResponseCartApiErrorException;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.DeleteCartData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.UpdateToSingleAddressShipmentData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.transaction.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.transaction.checkout.domain.usecase.ICartListInteractor;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemHolderData;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
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

    @SuppressWarnings("deprecation")
    @Override
    public void processInitialGetCartData() {
        view.renderLoadGetCartData();
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
                view.renderLoadGetCartDataFinish();
                if (e instanceof UnknownHostException) {
                    /* Ini kalau ga ada internet */
                    view.renderErrorNoConnectionInitialGetCartListData(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                    /* Ini kalau timeout */
                    view.renderErrorTimeoutConnectionInitialGetCartListData(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof ResponseErrorException) {
                     /* Ini kalau error dari API kasih message error */
                    view.renderErrorInitialGetCartListData(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    view.renderErrorInitialGetCartListData(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    /* Ini Http error, misal 403, 500, 404,
                     code http errornya bisa diambil
                     e.getErrorCode */
                    view.renderErrorHttpInitialGetCartListData(e.getMessage());
                } else if (e instanceof ResponseCartApiErrorException) {
                    view.renderErrorInitialGetCartListData(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    view.renderErrorHttpInitialGetCartListData(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(CartListData cartListData) {
                view.renderLoadGetCartDataFinish();
                if (cartListData.isError()) {
                    view.renderErrorInitialGetCartListData(cartListData.getErrorMessage());
                } else {
                    if (cartListData.getCartItemDataList().isEmpty()) {
                        view.renderEmptyCartData();
                    } else {
                        view.renderPromoVoucher();
                        view.renderInitialGetCartListDataSuccess(cartListData);
                    }
                }


            }
        }, view.getGeneratedAuthParamNetwork(param));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void processDeleteCart(final CartItemData cartItemData, final boolean addWishList) {
        view.showProgressLoading();
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
                e.printStackTrace();
                view.hideProgressLoading();
                if (e instanceof UnknownHostException) {
                    /* Ini kalau ga ada internet */
                    view.renderErrorNoConnectionActionDeleteCartData(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                    /* Ini kalau timeout */
                    view.renderErrorTimeoutConnectionActionDeleteCartData(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof ResponseErrorException) {
                     /* Ini kalau error dari API kasih message error */
                    view.renderErrorActionDeleteCartData(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    view.renderErrorActionDeleteCartData(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    /* Ini Http error, misal 403, 500, 404,
                     code http errornya bisa diambil
                     e.getErrorCode */
                    view.renderErrorHttpActionDeleteCartData(e.getMessage());
                } else if (e instanceof ResponseCartApiErrorException) {
                    view.renderErrorActionDeleteCartData(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    view.renderErrorHttpActionDeleteCartData(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(DeleteCartData deleteCartData) {
                view.hideProgressLoading();
                if (deleteCartData.isSuccess())
                    view.renderActionDeleteCartDataSuccess(
                            cartItemData, deleteCartData.getMessage(), addWishList
                    );
                else
                    view.renderErrorActionDeleteCartData(deleteCartData.getMessage());
            }
        }, view.getGeneratedAuthParamNetwork(param));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void processToShipmentSingleAddress() {
        view.showProgressLoading();
        List<CartItemData> cartItemDataList = view.getCartDataList();
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
                        e.printStackTrace();
                        view.hideProgressLoading();
                        if (e instanceof UnknownHostException) {
                    /* Ini kalau ga ada internet */
                            view.renderErrorNoConnectionToShipmentForm(
                                    ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                            );
                        } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                    /* Ini kalau timeout */
                            view.renderErrorTimeoutConnectionToShipmentForm(
                                    ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                            );
                        } else if (e instanceof ResponseErrorException) {
                     /* Ini kalau error dari API kasih message error */
                            view.renderErrorToShipmentForm(e.getMessage());
                        } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                            view.renderErrorToShipmentForm(e.getMessage());
                        } else if (e instanceof HttpErrorException) {
                    /* Ini Http error, misal 403, 500, 404,
                     code http errornya bisa diambil
                     e.getErrorCode */
                            view.renderErrorHttpToShipmentForm(e.getMessage());
                        } else if (e instanceof ResponseCartApiErrorException) {
                            view.renderErrorToShipmentForm(e.getMessage());
                        } else {
                    /* Ini diluar dari segalanya hahahaha */
                            view.renderErrorHttpToShipmentForm(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                    }

                    @Override
                    public void onNext(UpdateToSingleAddressShipmentData data) {
                        view.hideProgressLoading();
                        if (data.getUpdateCartData().isSuccess() && !data.getShipmentAddressFormData().isError()) {
                            view.renderToShipmentFormSuccess(data.getShipmentAddressFormData());
                        } else {
                            String messageError = !data.getShipmentAddressFormData().getErrorMessage().isEmpty()
                                    ? data.getShipmentAddressFormData().getErrorMessage()
                                    : data.getUpdateCartData().getMessage();
                            view.renderErrorToShipmentForm(messageError);
                        }
                    }
                },
                view.getGeneratedAuthParamNetwork(paramUpdate),
                view.getGeneratedAuthParamNetwork(paramGetShipmentForm)
        );

    }

    @SuppressWarnings("deprecation")
    @Override
    public void processToShipmentMultipleAddress(final RecipientAddressModel selectedAddress) {
        view.showProgressLoading();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("lang", "id");
        param.put("isReset", "false");
        cartListInteractor.getCartList(new Subscriber<CartListData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideProgressLoading();
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    /* Ini kalau ga ada internet */
                    view.renderErrorNoConnectionToShipmentMultipleAddress(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                    /* Ini kalau timeout */
                    view.renderErrorTimeoutConnectionToShipmentMultipleAddress(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof ResponseErrorException) {
                     /* Ini kalau error dari API kasih message error */
                    view.renderErrorToShipmentMultipleAddress(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    view.renderErrorToShipmentMultipleAddress(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    /* Ini Http error, misal 403, 500, 404,
                     code http errornya bisa diambil
                     e.getErrorCode */
                    view.renderErrorHttpToShipmentMultipleAddress(e.getMessage());
                } else if (e instanceof ResponseCartApiErrorException) {
                    view.renderErrorToShipmentMultipleAddress(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    view.renderErrorHttpToShipmentMultipleAddress(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(CartListData cartListData) {
                view.hideProgressLoading();
                if (!cartListData.isError())
                    view.renderToShipmentMultipleAddressSuccess(cartListData, selectedAddress);
                else
                    view.renderErrorToShipmentMultipleAddress(cartListData.getErrorMessage());
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

    @Override
    public void processCheckPromoCodeFromSuggestedPromo(String promoCode) {
        view.showProgressLoading();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("promo_code", promoCode);
        param.put("lang", "id");
        cartListInteractor.checkPromoCodeCartList(new Subscriber<PromoCodeCartListData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.hideProgressLoading();
            }

            @Override
            public void onNext(PromoCodeCartListData promoCodeCartListData) {
                view.hideProgressLoading();
                if (!promoCodeCartListData.isError())
                    view.renderCheckPromoCodeFromSuggestedPromoSuccess(promoCodeCartListData);
                else
                    view.renderErrorCheckPromoCodeFromSuggestedPromo(promoCodeCartListData.getErrorMessage());
            }
        }, view.getGeneratedAuthParamNetwork(param));
    }

    @Override
    public void processToShipmentForm() {
        view.showProgressLoading();
        TKPDMapParam<String, String> paramGetShipmentForm = new TKPDMapParam<>();
        paramGetShipmentForm.put("lang", "id");
        cartListInteractor.getShipmentForm(new Subscriber<CartShipmentAddressFormData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.hideProgressLoading();
                if (e instanceof UnknownHostException) {
                    /* Ini kalau ga ada internet */
                    view.renderErrorNoConnectionToShipmentForm(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                    /* Ini kalau timeout */
                    view.renderErrorTimeoutConnectionToShipmentForm(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof ResponseErrorException) {
                     /* Ini kalau error dari API kasih message error */
                    view.renderErrorToShipmentForm(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    view.renderErrorToShipmentForm(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    /* Ini Http error, misal 403, 500, 404,
                     code http errornya bisa diambil
                     e.getErrorCode */
                    view.renderErrorHttpToShipmentForm(e.getMessage());
                } else if (e instanceof ResponseCartApiErrorException) {
                    view.renderErrorToShipmentForm(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    view.renderErrorHttpToShipmentForm(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(CartShipmentAddressFormData cartShipmentAddressFormData) {
                view.hideProgressLoading();
                if (cartShipmentAddressFormData.isError()) {
                    view.renderErrorToShipmentForm(cartShipmentAddressFormData.getErrorMessage());
                } else {
                    view.renderToShipmentFormSuccess(cartShipmentAddressFormData);
                }
            }
        }, view.getGeneratedAuthParamNetwork(paramGetShipmentForm));
    }
}
