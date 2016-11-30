package com.tokopedia.transaction.cart.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.cart.interactor.CartDataInteractor;
import com.tokopedia.transaction.cart.interactor.ICartDataInteractor;
import com.tokopedia.transaction.cart.model.paramcheckout.CheckoutData;
import com.tokopedia.transaction.cart.model.paramcheckout.CheckoutDropShipperData;
import com.tokopedia.transaction.cart.model.toppaydata.TopPayParameterData;
import com.tokopedia.transaction.cart.receivers.CartBroadcastReceiver;
import com.tokopedia.transaction.exception.ResponseErrorException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * @author anggaprasetiyo on 11/28/16.
 */

public class CartIntentService extends IntentService {
    public static final String EXTRA_ACTION = "EXTRA_ACTION";
    public static final String EXTRA_CHECKOUT_DATA = "EXTRA_CHECKOUT_DATA";
    public static final int SERVICE_ACTION_NO_DEFINED = 0;
    public static final int SERVICE_ACTION_GET_PARAMETER_DATA = 1;

    private ICartDataInteractor cartDataInteractor;

    public CartIntentService() {
        super(CartIntentService.class.getCanonicalName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int extraAction = intent.getIntExtra(EXTRA_ACTION, SERVICE_ACTION_NO_DEFINED);
        switch (extraAction) {
            case SERVICE_ACTION_GET_PARAMETER_DATA:
                CheckoutData checkoutData = intent.getParcelableExtra(EXTRA_CHECKOUT_DATA);
                getParameterDataTopPay(checkoutData);
                break;
        }
    }

    private void getParameterDataTopPay(CheckoutData checkoutData) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put("lp_flag", checkoutData.getLpFlag());
        params.put("gateway", checkoutData.getGateway());
        params.put("step", "1");
        params.put("token", checkoutData.getToken());
        params.put("token_cart", checkoutData.getToken());
        params.put("chosen", checkoutData.getPartialString());
        params.put("method", "POST");
        params.put("use_deposit", "0");
        params.put("deposit_amt", checkoutData.getDepositAmount());
        params.put("partial_str", checkoutData.getPartialString());
        params.put("dropship_str", checkoutData.getDropShipString());
        if (checkoutData.getVoucherCode() != null) {
            params.put("voucher_code", checkoutData.getVoucherCode());
        }
        for (CheckoutDropShipperData data : checkoutData.getDropShipperDataList()) {
            params.put(data.getKey(), data.getValue());
        }

        if (cartDataInteractor == null) cartDataInteractor = new CartDataInteractor();
        Intent intent = new Intent(CartBroadcastReceiver.ACTION_TOP_PAY);
        intent.putExtra(CartBroadcastReceiver.EXTRA_RESULT_CODE_TOP_PAY_ACTION,
                CartBroadcastReceiver.RESULT_CODE_TOP_PAY_PROCESS_ONGOING);
        intent.putExtra(CartBroadcastReceiver.EXTRA_MESSAGE_TOP_PAY_ACTION,
                "Melakukan proses checkout");
        sendBroadcast(intent);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            Log.d("PARAM CART", entry.getKey() + " = " + entry.getValue());
        }
        cartDataInteractor.getParameterTopPay(
                AuthUtil.generateParamsNetwork(this, params), Schedulers.immediate(),
                new Subscriber<TopPayParameterData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Intent intent = new Intent(CartBroadcastReceiver.ACTION_TOP_PAY);
                        intent.putExtra(CartBroadcastReceiver.EXTRA_RESULT_CODE_TOP_PAY_ACTION,
                                CartBroadcastReceiver.RESULT_CODE_TOP_PAY_ERROR);
                        if (e instanceof UnknownHostException) {
                            intent.putExtra(CartBroadcastReceiver.EXTRA_MESSAGE_TOP_PAY_ACTION,
                                    ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                        } else if (e instanceof SocketTimeoutException) {
                            intent.putExtra(CartBroadcastReceiver.EXTRA_MESSAGE_TOP_PAY_ACTION,
                                    ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                        } else {
                            intent.putExtra(CartBroadcastReceiver.EXTRA_MESSAGE_TOP_PAY_ACTION,
                                    e.getCause() instanceof ResponseErrorException ? e.getMessage()
                                            : ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                        sendBroadcast(intent);
                    }

                    @Override
                    public void onNext(TopPayParameterData topPayParameterData) {
                        Intent intent = new Intent(CartBroadcastReceiver.ACTION_TOP_PAY);
                        intent.putExtra(CartBroadcastReceiver.EXTRA_RESULT_CODE_TOP_PAY_ACTION,
                                CartBroadcastReceiver.RESULT_CODE_TOP_PAY_SUCCESS);
                        intent.putExtra(
                                CartBroadcastReceiver.EXTRA_TOP_PAY_PARAMETER_DATA_TOP_PAY_ACTION,
                                topPayParameterData
                        );
                        sendBroadcast(intent);
                    }
                }
        );
    }
}
