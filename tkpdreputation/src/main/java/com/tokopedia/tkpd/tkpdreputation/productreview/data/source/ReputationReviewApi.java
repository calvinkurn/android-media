package com.tokopedia.tkpd.tkpdreputation.productreview.data.source;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.DataResponseReviewCount;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.DataResponseReviewHelpful;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.DataResponseReviewProduct;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.DataResponseReviewShop;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewstarcount.DataResponseReviewStarCount;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public interface ReputationReviewApi {

    @GET(TkpdBaseURL.Reputation.PATH_GET_REVIEW_PRODUCT_LIST)
    Observable<Response<DataResponseReviewProduct>> getReviewProductList(@FieldMap Map<String, Object> params);

    @GET(TkpdBaseURL.Reputation.PATH_GET_REVIEW_SHOP_LIST)
    Observable<Response<DataResponseReviewShop>> getReviewShopList(@FieldMap Map<String, Object> params);

    @GET(TkpdBaseURL.Reputation.PATH_GET_REVIEW_HELPFUL_LIST)
    Observable<Response<DataResponseReviewHelpful>> getReviewHelpfulList(@FieldMap Map<String, Object> params);

    @GET(TkpdBaseURL.Reputation.PATH_GET_REVIEW_PRODUCT_RATING)
    Observable<Response<DataResponseReviewStarCount>> getReviewStarCount(@FieldMap Map<String, Object> params);

    @GET(TkpdBaseURL.Reputation.PATH_GET_REVIEW_PRODUCT_COUNT + "{id}")
    Observable<Response<DataResponseReviewCount>> getReviewCount(@Path("id") String idProduct ,@FieldMap Map<String, Object> params);
}
