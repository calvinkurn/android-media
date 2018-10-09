package com.tokopedia.network.service;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by nisie on 3/10/17.
 */

public interface UploadImageApi {

    @Multipart
    @POST("")
    Observable<Response<TkpdResponse>> uploadImage(@Url String url,
                                                   @PartMap Map<String, RequestBody> params,
                                                   @Part("fileToUpload\"; filename=\"image.jpg") RequestBody imageFile);
}
