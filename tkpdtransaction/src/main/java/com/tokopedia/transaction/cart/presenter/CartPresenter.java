package com.tokopedia.transaction.cart.presenter;

import android.app.Activity;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.payment.interactor.PaymentNetInteractor;
import com.tokopedia.core.payment.interactor.PaymentNetInteractorImpl;
import com.tokopedia.transaction.cart.interactor.CartDataInteractor;
import com.tokopedia.transaction.cart.interactor.ICartDataInteractor;
import com.tokopedia.transaction.cart.listener.ICartView;
import com.tokopedia.transaction.cart.model.cartdata.CartModel;
import com.tokopedia.transaction.cart.model.cartdata.TransactionList;

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
    public void processGetCartData(Activity activity) {
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
    public void processCancelCart(Activity activity, TransactionList data) {
        TKPDMapParam<String, String> maps = new TKPDMapParam<>();
        maps.put("address_id", data.getCartDestination().getAddressId());
        maps.put("shipment_id", data.getCartShipments().getShipmentId());
        maps.put("shipment_package_id", data.getCartShipments().getShipmentPackageId());
        maps.put("shop_id", data.getCartShop().getShopId());
        cartDataInteractor.cancelCart(maps, new TKPDMapParam<String, String>(), new Subscriber<CartModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

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
}
