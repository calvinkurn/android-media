package com.tokopedia.tkpd.tkpdreputation.productreview.data.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
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
    Observable<Response<DataResponse<DataResponseReviewProduct>>> getReviewProductList(@FieldMap Map<String, String> params);

    @GET(TkpdBaseURL.Reputation.PATH_GET_REVIEW_SHOP_LIST)
    Observable<Response<DataResponse<DataResponseReviewShop>>> getReviewShopList(@FieldMap Map<String, Object> params);

    @GET(TkpdBaseURL.Reputation.PATH_GET_REVIEW_HELPFUL_LIST)
    Observable<Response<DataResponse<DataResponseReviewHelpful>>> getReviewHelpfulList(@FieldMap Map<String, String> params);

    @GET(TkpdBaseURL.Reputation.PATH_GET_REVIEW_PRODUCT_RATING)
    Observable<Response<DataResponse<DataResponseReviewStarCount>>> getReviewStarCount(@FieldMap Map<String, String> params);
}
