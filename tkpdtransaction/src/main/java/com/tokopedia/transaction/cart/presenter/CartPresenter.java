package com.tokopedia.transaction.cart.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.payment.interactor.PaymentNetInteractor;
import com.tokopedia.core.payment.interactor.PaymentNetInteractorImpl;
import com.tokopedia.transaction.cart.interactor.CartDataInteractor;
import com.tokopedia.transaction.cart.interactor.ICartDataInteractor;
import com.tokopedia.transaction.cart.listener.ICartView;
import com.tokopedia.transaction.cart.model.calculateshipment.ProductEditData;
import com.tokopedia.transaction.cart.model.cartdata.CartModel;
import com.tokopedia.transaction.cart.model.cartdata.CartProduct;
import com.tokopedia.transaction.cart.model.cartdata.TransactionList;

import java.util.List;

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
        TKPDMapParam<String, String> params = AuthUtil.generateParamsNetwork(activity);
        cartDataInteractor.getCartData(params, new Subscriber<CartModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(CartModel data) {
                view.renderDepositInfo(data.getDepositIdr());
                view.renderTotalPayment(data.getGrandTotalWithoutLPIDR());
                view.renderPaymentGatewayOption(data.getGatewayList());
                view.renderLoyaltyBalance(data.getLpAmountIdr(), data.getLpAmount() != 0);
                view.renderCartListData(data.getTransactionLists());
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
                new Subscriber<CartModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.hideProgressLoading();
                    }

                    @Override
                    public void onNext(CartModel data) {
                        view.renderDepositInfo(data.getDepositIdr());
                        view.renderTotalPayment(data.getGrandTotalWithoutLPIDR());
                        view.renderPaymentGatewayOption(data.getGatewayList());
                        view.renderLoyaltyBalance(data.getLpAmountIdr(), data.getLpAmount() != 0);
                        view.renderCartListData(data.getTransactionLists());
                        view.hideProgressLoading();
                    }
                });
    }

    @Override
    public void processCancelCartProduct(@NonNull Activity activity, @NonNull TransactionList cartData,
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
                new Subscriber<CartModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.hideProgressLoading();
                    }

                    @Override
                    public void onNext(CartModel data) {
                        view.renderDepositInfo(data.getDepositIdr());
                        view.renderTotalPayment(data.getGrandTotalWithoutLPIDR());
                        view.renderPaymentGatewayOption(data.getGatewayList());
                        view.renderLoyaltyBalance(data.getLpAmountIdr(), data.getLpAmount() != 0);
                        view.renderCartListData(data.getTransactionLists());
                        view.hideProgressLoading();
                    }
                });
    }

    @Override
    public void processSubmitEditCart(Activity activity, TransactionList cartData,
                                      List<ProductEditData> cartProductEditDataList) {
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
                new Subscriber<CartModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.hideProgressLoading();
                    }

                    @Override
                    public void onNext(CartModel data) {
                        view.renderDepositInfo(data.getDepositIdr());
                        view.renderTotalPayment(data.getGrandTotalWithoutLPIDR());
                        view.renderPaymentGatewayOption(data.getGatewayList());
                        view.renderLoyaltyBalance(data.getLpAmountIdr(), data.getLpAmount() != 0);
                        view.renderCartListData(data.getTransactionLists());
                        view.hideProgressLoading();
                    }
                });
    }
}
