package com.tokopedia.transaction.bcaoneklik.domain;

import com.tokopedia.core.network.apiservices.payment.BcaOneClickService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.BcaOneClickData;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.BcaOneClickSuccessRegisterData;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.PaymentListModel;
import com.tokopedia.transaction.exception.ResponseRuntimeException;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 7/25/17. Tokopedia
 */

public class BcaOneClickFormRepository implements IBcaOneClickFormRepository {

    private BcaOneClickService bcaOneClickService;

    public BcaOneClickFormRepository(BcaOneClickService bcaOneClickService) {
        this.bcaOneClickService = bcaOneClickService;
    }

    @Override
    public Observable<BcaOneClickData> getBcaOneClickAccessToken(
            TKPDMapParam<String, String> bcaOneClickParam
    ) {
        return bcaOneClickService.getApi().accessBcaOneClick(bcaOneClickParam)
                .map(new Func1<Response<TkpdResponse>, BcaOneClickData>() {
                    @Override
                    public BcaOneClickData call(Response<TkpdResponse> response) {
                        handlerError(response);
                        return response.body().convertDataObj(BcaOneClickData.class);
                    }
                });
    }

    @Override
    public Observable<PaymentListModel> getPaymentListUserData(TKPDMapParam<String, String> oneClickListParam) {
        return bcaOneClickService.getApi().getBcaOneClickUserData(oneClickListParam)
                .map(new Func1<Response<TkpdResponse>, PaymentListModel>() {
                    @Override
                    public PaymentListModel call(Response<TkpdResponse> response) {
                        handlerError(response);
                        return response.body().convertDataObj(PaymentListModel.class);
                    }
                });
    }

    private void handlerError(Response<TkpdResponse> response) {
        if (response.body() == null)
            throw new ResponseRuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT_SHORT);
        else if(response.body().isNullData()) {
            throw new ResponseRuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT_SHORT);
        } else if(response.body().isError()) {
            throw new ResponseRuntimeException(response.body()
                    .getErrorMessageJoined());
        } else if(response.body().getStatus().equals("INVALID_REQUEST")) {
            throw new ResponseRuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT_SHORT);
        }
    }

    @Override
    public Observable<PaymentListModel> deleteUserData(TKPDMapParam<String, String> oneClickListParam) {
        return bcaOneClickService.getApi().deleteBcaOneClickUserData(oneClickListParam)
                .map(new Func1<Response<TkpdResponse>, PaymentListModel>() {
                    @Override
                    public PaymentListModel call(Response<TkpdResponse> response) {
                        handlerError(response);
                        return response.body().convertDataObj(PaymentListModel.class);
                    }
                });
    }


    @Override
    public Observable<BcaOneClickSuccessRegisterData> registerBcaOneClickData(
            TKPDMapParam<String, String> bcaOneClickRegisterParam) {

        return bcaOneClickService.getApi().registerBcaOneClickUserData(bcaOneClickRegisterParam)
                .map(new Func1<Response<TkpdResponse>, BcaOneClickSuccessRegisterData>() {
                    @Override
                    public BcaOneClickSuccessRegisterData call(Response<TkpdResponse> stringResponse) {
                        handlerError(stringResponse);
                        return stringResponse.body().convertDataObj(BcaOneClickSuccessRegisterData.class);
                    }
                });
    }

    @Override
    public Observable<PaymentListModel> editBcaOneClickData(TKPDMapParam<String, String> bcaOneClickEditParam) {
        return bcaOneClickService.getApi().editBcaOneClickUserData(bcaOneClickEditParam)
                .map(new Func1<Response<TkpdResponse>, PaymentListModel>() {
                    @Override
                    public PaymentListModel call(Response<TkpdResponse> response) {
                        handlerError(response);
                        return response.body().convertDataObj(PaymentListModel.class);
                    }
                });
    }

}
