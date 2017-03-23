package com.tokopedia.seller.shop.setting.data.source.cloud.apiservice;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.seller.shop.setting.data.model.GenerateHostModel;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zulfikarrahman on 3/22/17.
 */

public interface GenerateHostApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Upload.PATH_GENERATE_HOST)
    Observable<Response<GenerateHostModel>> generateHost(@FieldMap Map<String, String> params);
}
