package com.tokopedia.transaction.purchase.domain;

import com.google.gson.Gson;
import com.tokopedia.core.network.apiservices.payment.PaymentTransactionService;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.exception.ResponseRuntimeException;
import com.tokopedia.transaction.purchase.model.response.canceltransaction.CancelTransactionResponse;
import com.tokopedia.transaction.purchase.model.response.canceltransaction.CancelationDialogResponse;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 9/13/17. Tokopedia
 */

public class TxVerificationRepository implements ITxVerificationRepository{

    private static final int SUCCESS_CODE = 200;
    private static final int EXPIRE_CODE = 404003;
    private static final int CANCELLED_CODE = 404004;
    private PaymentTransactionService service;

    public TxVerificationRepository(PaymentTransactionService service) {
        this.service = service;
    }

    @Override
    public Observable<String> processCancelDialogResponse(TKPDMapParam<String, String> showCancelDialogParam) {
        return service.getApi().showPaymentCancelDialog(showCancelDialogParam)
                .map(new Func1<Response<String>, String>() {
                    @Override
                    public String call(Response<String> stringResponse) {
                        CancelationDialogResponse response = (new Gson().fromJson(stringResponse.body(),
                                CancelationDialogResponse.class));
                        handlerFetchError(stringResponse);
                        if(response.getResponseCode() != SUCCESS_CODE) {
                            handleDataError(false, response.getData().getMessage());
                        }
                        return response.getData().getMessage();
                    }
                });
    }

    @Override
    public Observable<String> processCancelTransaction(TKPDMapParam<String, String> cancellationParam) {
        return service.getApi().cancelTransaction(cancellationParam)
                .map(new Func1<Response<String>, String>() {
                    @Override
                    public String call(Response<String> stringResponse) {
                        CancelTransactionResponse response = (new Gson()
                                .fromJson(stringResponse.body(),
                                CancelTransactionResponse.class));
                        handlerFetchError(stringResponse);
                        if(response.getResponseCode() != SUCCESS_CODE
                                && response.getResponseCode() != EXPIRE_CODE
                                && response.getResponseCode() != CANCELLED_CODE) {
                            handleDataError(false, response.getMessage());
                        }
                        return response.getMessage();
                    }
                });
    }

    private void handlerFetchError(Response<String> response) {
        if (response.body() == null)
            throw new ResponseRuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT_SHORT);
    }

    private void handleDataError(boolean isSuccess, String message) {
        if(!isSuccess) {
            throw new ResponseRuntimeException(message);
        }
    }
}
