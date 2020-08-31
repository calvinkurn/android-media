package com.tokopedia.seller.manageitem.data.cloud.api;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by zulfikarrahman on 12/19/17.
 */

public interface ImageUploadApi {
    @Multipart
    @POST
    Observable<Response<String>> uploadImage(@Url String url, @PartMap Map<String, RequestBody> params);
}
