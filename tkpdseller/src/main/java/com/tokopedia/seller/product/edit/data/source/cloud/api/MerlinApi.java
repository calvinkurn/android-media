package com.tokopedia.seller.product.edit.data.source.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.seller.product.edit.data.source.cloud.api.request.CategoryRecommRequest;
import com.tokopedia.seller.product.edit.data.source.cloud.model.categoryrecommdata.CategoryRecommDataModel;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Hendry on 4/5/2017.
 */

public interface MerlinApi {

    @POST(TkpdBaseURL.Merlin.PATH_CATEGORY_RECOMMENDATION)
    Observable<CategoryRecommDataModel> getCategoryRecomm(@Body CategoryRecommRequest categoryRecommRequest);
}
