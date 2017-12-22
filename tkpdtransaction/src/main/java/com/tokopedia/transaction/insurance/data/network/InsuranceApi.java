package com.tokopedia.transaction.insurance.data.network;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Irfan Khoirul on 12/12/17.
 */

public interface InsuranceApi {

    @GET(TkpdBaseURL.Shop.PATH_INSURANCE_TERMS_AND_CONDITIONS)
    Observable<String> getInsuranceTnC();

}
