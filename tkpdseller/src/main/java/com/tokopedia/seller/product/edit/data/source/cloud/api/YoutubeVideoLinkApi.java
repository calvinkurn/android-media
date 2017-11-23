package com.tokopedia.seller.product.edit.data.source.cloud.api;

import com.tokopedia.seller.product.edit.data.source.cloud.model.youtube.YoutubeResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by normansyahputa on 4/11/17.
 */

public interface YoutubeVideoLinkApi {
    @GET("/youtube/v3/videos")
    Observable<Response<YoutubeResponse>> getVideoDetail(@QueryMap Map<String, String> params);
}
