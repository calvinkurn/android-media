package com.tokopedia.tkpdpdp.service;

import com.tokopedia.tkpdpdp.responsemodel.ResponseModel;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by henrypriyono on 05/03/18.
 */

public interface ImageService {
    @GET("search/product/v3?device=android&ob=23&q=sepatu&rows=20&source=search")
    Observable<ResponseModel> loadData(@Query("start") int start);
}
