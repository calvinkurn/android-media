package com.tokopedia.seller.product.imagepicker;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.seller.product.imagepicker.model.DataResponseCatalogImage;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public interface CatalogApi {
    @GET(CatalogConstant.URL_GET_CATALOG_IMAGE + "{id}")
    Observable<Response<DataResponse<DataResponseCatalogImage>>> getCatalogImage(@Path("id") String catalogId);
}
