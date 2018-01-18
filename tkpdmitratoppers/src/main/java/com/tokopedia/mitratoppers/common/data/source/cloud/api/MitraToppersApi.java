package com.tokopedia.mitratoppers.common.data.source.cloud.api;

import com.tokopedia.mitratoppers.common.constant.MitraToppersBaseURL;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Hendry on 4/20/2017.
 */

public interface MitraToppersApi {

    @GET(MitraToppersBaseURL.PATH_PREAPPROVE_BALANCE + "{id}" )
    Observable<Response<String>> preApproveBalance(@Path("id") String merchantId);
}
