package com.tokopedia.transaction.checkout.data.repository;

import com.tokopedia.core.network.apiservices.transaction.TXActService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.cart.model.thankstoppaydata.ThanksTopPayData;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 06/03/18.
 */

public class TopPayRepository implements ITopPayRepository {

    private final TXActService txActService;

    @Inject
    public TopPayRepository(TXActService txActService) {
        this.txActService = txActService;
    }

    @Override
    public Observable<ThanksTopPayData> getThanksTopPay(TKPDMapParam<String, String> param) {
        return txActService.getApi().getThanksDynamicPayment(param).map(
                new Func1<Response<TkpdResponse>, ThanksTopPayData>() {
                    @Override
                    public ThanksTopPayData call(Response<TkpdResponse> tkpdResponseResponse) {
                        return tkpdResponseResponse.body().convertDataObj(ThanksTopPayData.class);
                    }
                });
    }
}
