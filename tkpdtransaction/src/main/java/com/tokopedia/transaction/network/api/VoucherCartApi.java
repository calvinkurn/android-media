package com.tokopedia.transaction.network.api;

import com.tokopedia.transaction.network.TransactionUrl;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by kris on 3/29/18. Tokopedia
 */

public interface VoucherCartApi {

    @FormUrlEncoded
    @POST(TransactionUrl.PATH_CLEAR_PROMO)
    Observable<String> checkVoucherCode(@FieldMap Map<String, String> params);

}
