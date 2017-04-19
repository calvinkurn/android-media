package com.tokopedia.seller.product.data.source.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.seller.product.data.source.cloud.api.request.CategoryRecommRequest;
import com.tokopedia.seller.product.data.source.cloud.model.catalogdata.CatalogDataModel;
import com.tokopedia.seller.product.data.source.cloud.model.categoryrecommdata.CategoryRecommDataModel;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Hendry on 4/5/2017.
 */

public interface MerlinApi {

    @POST(TkpdBaseURL.Merlin.PATH_CATEGORY_RECOMMENDATION)
    Observable<CategoryRecommDataModel> getCategoryRecomm(@Body CategoryRecommRequest categoryRecommRequest);
}
