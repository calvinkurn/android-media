package com.tokopedia.transaction.cart.presenter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.payment.interactor.PaymentNetInteractor;
import com.tokopedia.core.payment.interactor.PaymentNetInteractorImpl;
import com.tokopedia.transaction.cart.interactor.CartDataInteractor;
import com.tokopedia.transaction.cart.interactor.ICartDataInteractor;
import com.tokopedia.transaction.cart.listener.ICartView;
import com.tokopedia.transaction.cart.model.CartItemEditable;
import com.tokopedia.transaction.cart.model.ResponseTransform;
import com.tokopedia.transaction.cart.model.calculateshipment.ProductEditData;
import com.tokopedia.transaction.cart.model.cartdata.CartModel;
import com.tokopedia.transaction.cart.model.cartdata.CartProduct;
import com.tokopedia.transaction.cart.model.cartdata.TransactionList;
import com.tokopedia.transaction.cart.model.paramcheckout.CheckoutData;
import com.tokopedia.transaction.cart.model.paramcheckout.CheckoutDropShipperData;
import com.tokopedia.transaction.cart.model.toppaydata.TopPayParameterData;
import com.tokopedia.transaction.cart.model.voucher.VoucherData;
import com.tokopedia.transaction.cart.services.CartIntentService;
import com.tokopedia.transaction.exception.HttpErrorException;
import com.tokopedia.transaction.exception.ResponseErrorException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 11/3/16.
 */

public class CartPresenter implements ICartPresenter {
    private final ICartView view;
    private final ICartDataInteractor cartDataInteractor;
    private final PaymentNetInteractor paymentNetInteractor;

    public CartPresenter(ICartView iCartView) {
        this.view = iCartView;
        this.cartDataInteractor = new CartDataInteractor();
        this.paymentNetInteractor = new PaymentNetInteractorImpl();
    }

    @Override
    public void processGetCartData(@NonNull Activity activity) {
        view.renderInitialLoadingCartInfo();
        TKPDMapParam<String, String> params = AuthUtil.generateParamsNetwork(activity);
        cartDataInteractor.getCartData(params, new Subscriber<ResponseTransform<CartModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(ResponseTransform<CartModel> responseTransform) {
                CartModel data = responseTransform.getData();
                if (data.getTransactionLists().isEmpty()) {
                    view.renderEmptyCart();
                    return;
                }
                view.renderVisibleMainCartContainer();
                if (data.getCashback() != 0)
                    view.renderVisiblePotentialCashBack(data.getCashbackIdr());
                else view.renderGonePotentialCashBack();
                view.renderDepositInfo(data.getDepositIdr());
                view.renderTotalPayment(data.getGrandTotalWithoutLPIDR());
                view.renderPaymentGatewayOption(data.getGatewayList());
                view.renderLoyaltyBalance(data.getLpAmountIdr(), data.getLpAmount() != 0);
                view.renderCartListData(data.getTransactionLists());
                view.renderCheckoutCartDepositAmount(data.getDeposit() + "");
                view.renderCheckoutCartToken(data.getToken());
                view.renderErrorPaymentCart(
                        (data.getCheckoutNotifError() != null && !data.getCheckoutNotifError().equals("0")
                        ), data.getCheckoutNotifError()
                );
                view.renderButtonCheckVoucherListener();
            }
        });
    }

    @Override
    public void processCancelCart(@NonNull Activity activity, @NonNull TransactionList data) {
        view.showProgressLoading();
        TKPDMapParam<String, String> maps = new TKPDMapParam<>();
        maps.put("address_id", data.getCartDestination().getAddressId());
        maps.put("shipment_id", data.getCartShipments().getShipmentId());
        maps.put("shipment_package_id", data.getCartShipments().getShipmentPackageId());
        maps.put("shop_id", data.getCartShop().getShopId());
        cartDataInteractor.cancelCart(AuthUtil.generateParamsNetwork(activity, maps),
                AuthUtil.generateParamsNetwork(activity),
                new Subscriber<ResponseTransform<CartModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.hideProgressLoading();
                    }

                    @Override
                    public void onNext(ResponseTransform<CartModel> responseTransform) {
                        view.hideProgressLoading();
                        CartModel data = responseTransform.getData();
                        view.showToastMessage(
                                responseTransform.getMessageSuccess().isEmpty()
                                        ? "Berhasil menghapus item keranjang"
                                        : responseTransform.getMessageSuccess()
                        );
                        if (data.getTransactionLists().isEmpty()) {
                            view.renderEmptyCart();
                            return;
                        }
                        view.renderDepositInfo(data.getDepositIdr());
                        if (data.getCashback() != 0)
                            view.renderVisiblePotentialCashBack(data.getCashbackIdr());
                        else view.renderGonePotentialCashBack();
                        view.renderTotalPayment(data.getGrandTotalWithoutLPIDR());
                        view.renderPaymentGatewayOption(data.getGatewayList());
                        view.renderLoyaltyBalance(data.getLpAmountIdr(), data.getLpAmount() != 0);
                        view.renderCartListData(data.getTransactionLists());
                        view.renderCheckoutCartDepositAmount(data.getDeposit() + "");
                        view.renderCheckoutCartToken(data.getToken());
                        view.renderErrorPaymentCart(
                                (data.getCheckoutNotifError() != null
                                        && !data.getCheckoutNotifError().equals("0")),
                                data.getCheckoutNotifError()
                        );
                        view.renderButtonCheckVoucherListener();

                    }
                });
    }

    @Override
    public void processCancelCartProduct(@NonNull Activity activity,
                                         @NonNull TransactionList cartData,
                                         @NonNull CartProduct cartProductData) {
        view.showProgressLoading();
        TKPDMapParam<String, String> maps = new TKPDMapParam<>();
        maps.put("product_cart_id", cartProductData.getProductCartId());
        maps.put("address_id", cartData.getCartDestination().getAddressId());
        maps.put("shipment_id", cartData.getCartShipments().getShipmentId());
        maps.put("shipment_package_id", cartData.getCartShipments().getShipmentPackageId());
        maps.put("shop_id", cartData.getCartShop().getShopId());
        cartDataInteractor.cancelCart(AuthUtil.generateParamsNetwork(activity, maps),
                AuthUtil.generateParamsNetwork(activity),
                new Subscriber<ResponseTransform<CartModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.hideProgressLoading();
                    }

                    @Override
                    public void onNext(ResponseTransform<CartModel> responseTransform) {
                        view.hideProgressLoading();
                        CartModel data = responseTransform.getData();
                        view.showToastMessage(responseTransform.getMessageSuccess().isEmpty()
                                ? "Berhasil menghapus item keranjang"
                                : responseTransform.getMessageSuccess());
                        if (data.getTransactionLists().isEmpty()) {
                            view.renderEmptyCart();
                            return;
                        }
                        if (data.getCashback() != 0)
                            view.renderVisiblePotentialCashBack(data.getCashbackIdr());
                        else view.renderGonePotentialCashBack();
                        view.renderDepositInfo(data.getDepositIdr());
                        view.renderTotalPayment(data.getGrandTotalWithoutLPIDR());
                        view.renderPaymentGatewayOption(data.getGatewayList());
                        view.renderLoyaltyBalance(data.getLpAmountIdr(), data.getLpAmount() != 0);
                        view.renderCartListData(data.getTransactionLists());
                        view.renderCheckoutCartDepositAmount(data.getDeposit() + "");
                        view.renderCheckoutCartToken(data.getToken());
                        view.renderErrorPaymentCart(
                                (data.getCheckoutNotifError() != null
                                        && !data.getCheckoutNotifError().equals("0")),
                                data.getCheckoutNotifError()
                        );
                        view.renderButtonCheckVoucherListener();
                    }
                });
    }

    @Override
    public void processSubmitEditCart(@NonNull Activity activity,
                                      @NonNull TransactionList cartData,
                                      @NonNull List<ProductEditData> cartProductEditDataList) {
        view.showProgressLoading();
        TKPDMapParam<String, String> maps = new TKPDMapParam<>();
        maps.put("carts", new Gson().toJson(cartProductEditDataList));
        maps.put("cart_shop_id", cartData.getCartShop().getShopId());
        maps.put("cart_addr_id", cartData.getCartDestination().getAddressId());
        maps.put("cart_shipping_id", cartData.getCartShipments().getShipmentId());
        maps.put("cart_sp_id", cartData.getCartShipments().getShipmentPackageId());
        maps.put("lp_flag", "1");
        maps.put("cart_string", cartData.getCartString());
        cartDataInteractor.updateCart(AuthUtil.generateParamsNetwork(activity, maps),
                AuthUtil.generateParamsNetwork(activity),
                new Subscriber<ResponseTransform<CartModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.hideProgressLoading();
                    }

                    @Override
                    public void onNext(ResponseTransform<CartModel> responseTransform) {
                        view.hideProgressLoading();
                        CartModel data = responseTransform.getData();
                        view.showToastMessage(
                                responseTransform.getMessageSuccess().isEmpty()
                                        ? "Berhasil mengubah keranjang"
                                        : responseTransform.getMessageSuccess()
                        );
                        if (data.getTransactionLists().isEmpty()) {
                            view.renderEmptyCart();
                            return;
                        }
                        if (data.getCashback() != 0)
                            view.renderVisiblePotentialCashBack(data.getCashbackIdr());
                        else view.renderGonePotentialCashBack();
                        view.renderDepositInfo(data.getDepositIdr());
                        view.renderTotalPayment(data.getGrandTotalWithoutLPIDR());
                        view.renderPaymentGatewayOption(data.getGatewayList());
                        view.renderLoyaltyBalance(data.getLpAmountIdr(), data.getLpAmount() != 0);
                        view.renderCartListData(data.getTransactionLists());
                        view.renderCheckoutCartDepositAmount(data.getDeposit() + "");
                        view.renderCheckoutCartToken(data.getToken());
                        view.renderErrorPaymentCart(
                                (data.getCheckoutNotifError() != null
                                        && !data.getCheckoutNotifError().equals("0")),
                                data.getCheckoutNotifError()
                        );
                        view.renderButtonCheckVoucherListener();
                    }
                });
    }

    @Override
    public void processUpdateInsurance(@NonNull Activity activity,
                                       @NonNull TransactionList cartData, boolean useInsurance) {
        view.showProgressLoading();
        TKPDMapParam<String, String> maps = new TKPDMapParam<>();
        maps.put("address_id", cartData.getCartDestination().getAddressId());
        maps.put("product_insurance", useInsurance ? "1" : "0");
        maps.put("shipment_id", cartData.getCartShipments().getShipmentId());
        maps.put("shipment_package_id", cartData.getCartShipments().getShipmentPackageId());
        maps.put("shop_id", cartData.getCartShop().getShopId());
        cartDataInteractor.updateInsuranceCart(AuthUtil.generateParamsNetwork(activity, maps),
                AuthUtil.generateParamsNetwork(activity),
                new Subscriber<ResponseTransform<CartModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.hideProgressLoading();
                    }

                    @Override
                    public void onNext(ResponseTransform<CartModel> responseTransform) {
                        view.hideProgressLoading();
                        CartModel data = responseTransform.getData();
                        view.showToastMessage(
                                responseTransform.getMessageSuccess().isEmpty()
                                        ? "Berhasil mengubah asuransi"
                                        : responseTransform.getMessageSuccess()
                        );
                        if (data.getTransactionLists().isEmpty()) {
                            view.renderEmptyCart();
                            return;
                        }
                        if (data.getCashback() != 0)
                            view.renderVisiblePotentialCashBack(data.getCashbackIdr());
                        else view.renderGonePotentialCashBack();
                        view.renderDepositInfo(data.getDepositIdr());
                        view.renderTotalPayment(data.getGrandTotalWithoutLPIDR());
                        view.renderPaymentGatewayOption(data.getGatewayList());
                        view.renderLoyaltyBalance(data.getLpAmountIdr(), data.getLpAmount() != 0);
                        view.renderCartListData(data.getTransactionLists());
                        view.renderCheckoutCartDepositAmount(data.getDeposit() + "");
                        view.renderCheckoutCartToken(data.getToken());
                        view.renderErrorPaymentCart(
                                (data.getCheckoutNotifError() != null
                                        && !data.getCheckoutNotifError().equals("0")),
                                data.getCheckoutNotifError()
                        );
                        view.renderButtonCheckVoucherListener();
                    }
                });
    }

    @Override
    public void processCheckoutCart(@NonNull Activity activity,
                                    @NonNull CheckoutData.Builder checkoutDataBuilder,
                                    @NonNull List<CartItemEditable> cartItemEditables) {
        List<String> dropShipperNameList = new ArrayList<>();
        List<String> dropShipperPhoneList = new ArrayList<>();

        List<String> dropShipperStringList = new ArrayList<>();
        List<String> partialDeliverStringList = new ArrayList<>();

        for (CartItemEditable data : cartItemEditables) {
            if (data.isDropShipper()) {
                dropShipperNameList.add(data.getDropShipperName());
                dropShipperPhoneList.add(data.getDropShipperPhone());
                dropShipperStringList.add(data.getCartStringForDropShipperOption());
            }
            if (data.isPartialDeliver()) {
                partialDeliverStringList.add(data.getCartStringForDeliverOption());
            }
        }

        StringBuilder dropShipperParamStringBuilder = new StringBuilder();
        for (String stringCart : dropShipperStringList) {
            dropShipperParamStringBuilder.append(stringCart).append("*~*");
        }
        String dropShipperParamString = dropShipperParamStringBuilder.toString();
        if (dropShipperParamString.endsWith("*~*")) {
            dropShipperParamString = dropShipperParamString.substring(0,
                    dropShipperParamString.lastIndexOf("*~*"));
        }

        StringBuilder partialDeliverParamBuilder = new StringBuilder();
        for (String stringCart : partialDeliverStringList) {
            partialDeliverParamBuilder.append(stringCart).append("*~*");
        }
        String partialDeliverParamString = partialDeliverParamBuilder.toString();
        if (partialDeliverParamString.endsWith("*~*")) {
            partialDeliverParamString = partialDeliverParamString.substring(0,
                    partialDeliverParamString.lastIndexOf("*~*"));
        }

        List<CheckoutDropShipperData> checkoutDropShipperDataList = new ArrayList<>();

        Map<String, String> dropShipperDataParam = generateDropShipperParam(dropShipperNameList,
                dropShipperPhoneList, dropShipperStringList);


        for (Map.Entry<String, String> entry : dropShipperDataParam.entrySet()) {
            checkoutDropShipperDataList.add(new CheckoutDropShipperData.Builder()
                    .key(entry.getKey())
                    .value(entry.getValue())
                    .build());
        }

        CheckoutData checkoutData = checkoutDataBuilder
                .dropShipperDataList(checkoutDropShipperDataList)
                .dropShipString(dropShipperParamString)
                .partialString(partialDeliverParamString)
                .lpFlag("1")
                .step("1")
                .build();

        Intent intent = new Intent(Intent.ACTION_SYNC, null, activity,
                CartIntentService.class);
        intent.putExtra(CartIntentService.EXTRA_ACTION,
                CartIntentService.SERVICE_ACTION_GET_PARAMETER_DATA);
        intent.putExtra(CartIntentService.EXTRA_CHECKOUT_DATA, checkoutData);
        activity.startService(intent);
    }

    @Override
    public void processCheckVoucherCode(@NonNull Activity activity, @NonNull String voucherCode) {
        view.showProgressLoading();
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put("voucher_code", voucherCode);
        cartDataInteractor.checkVoucherCode(AuthUtil.generateParamsNetwork(activity, params),
                new Subscriber<ResponseTransform<VoucherData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof SocketTimeoutException) {
                            view.showToastMessage(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                        } else if (e instanceof UnknownHostException) {
                            view.showToastMessage(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                        } else if (e.getCause() instanceof ResponseErrorException) {
                            view.renderErrorCheckVoucher(e.getCause().getMessage());
                        } else if (e.getCause() instanceof HttpErrorException) {
                            view.showToastMessage(e.getCause().getMessage());
                        } else {
                            view.showToastMessage(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                        view.hideProgressLoading();
                    }

                    @Override
                    public void onNext(ResponseTransform<VoucherData> voucherDataResponseTransform) {
                        view.renderSuccessVoucherChecked(
                                voucherDataResponseTransform.getMessageSuccess(),
                                voucherDataResponseTransform.getData()
                        );
                        view.hideProgressLoading();
                    }
                });
    }

    @Override
    public void processStep2PaymentCart(Activity activity, TopPayParameterData data) {

    }

    @Override
    public void processGetTickerGTM() {
        if (TrackingUtils.getGtmString(AppEventTracking.GTM.TICKER_CART).equalsIgnoreCase("true")) {
            String message = TrackingUtils.getGtmString(AppEventTracking.GTM.TICKER_CART_TEXT);
            view.renderVisibleTickerGTM(message);
        } else {
            view.renderGoneTickerGTM();
        }
    }

    @Override
    public void processValidationCheckoutData(Activity activity) {
        List<CartItemEditable> cartItemEditables = view.getItemCartListCheckoutData();
        boolean canBeCheckout = true;

        CheckoutData.Builder checkoutDataBuilder = view.getCheckoutDataBuilder();
        String voucherCode = view.getVoucherCodeCheckoutData();
        boolean isUseVoucher = view.isCheckoutDataUseVoucher();

        if (isUseVoucher && voucherCode.isEmpty()) {
            view.renderErrorCheckVoucher("Voucher Kosong");
            return;
        } else if (isUseVoucher && !voucherCode.isEmpty()) {
            view.renderDisableErrorCheckVoucher();
            checkoutDataBuilder.voucherCode(voucherCode);
        }

        String depositCheckout = view.getDepositCheckoutData();
        checkoutDataBuilder.usedDeposit(depositCheckout.replaceAll("\\D+", ""));

        for (int i = 0, cartItemEditablesSize = cartItemEditables.size();
             i < cartItemEditablesSize; i++) {
            CartItemEditable cartItemEditable = cartItemEditables.get(i);
            view.renderErrorCartItem(cartItemEditable);
            if (cartItemEditable.finalizeAllData().getErrorType()
                    != CartItemEditable.ERROR_NON) {
                canBeCheckout = false;
            }
        }
        if (!canBeCheckout) {
            view.showToastMessage("Keranjang tidak dapat diproses," +
                    " mohon periksa kembali keranjang Anda.");
        } else {
            processCheckoutCart(activity, checkoutDataBuilder, cartItemEditables);
        }
    }

    @NonNull
    private Map<String, String> generateDropShipperParam(List<String> dropShipperNameList,
                                                         List<String> dropShipperPhoneList,
                                                         List<String> dropShipperStringList) {
        Map<String, String> params = new HashMap<>();
        for (int i = 0, dropShipperNameListSize = dropShipperNameList.size();
             i < dropShipperNameListSize; i++) {
            params.put(
                    "dropship_name-" + dropShipperStringList.get(i), dropShipperNameList.get(i)
            );
            params.put(
                    "dropship_telp-" + dropShipperStringList.get(i), dropShipperPhoneList.get(i)
            );
        }
        return params;
    }
}
