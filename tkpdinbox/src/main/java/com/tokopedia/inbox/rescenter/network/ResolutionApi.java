package com.tokopedia.inbox.rescenter.network;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem.ProductProblemListResponse;

import java.util.HashMap;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author by yfsx on 26/07/18.
 */
public interface ResolutionApi {

    String PATH_RESOLUTION_ID = "resolution_id";
    String PATH_ORDER_ID = "order_id";
    String PATH_TROUBLE_ID = "trouble_id";
    String PATH_CONVERSATION_ID = "conversation_id";

    @GET(ResolutionUrl.GET_RESOLUTION_STEP_1)
    Observable<Response<DataResponse<ProductProblemListResponse>>> getProductProblemList(@Path(PATH_ORDER_ID) String orderId,
                                                                                         @QueryMap HashMap<String, Object> params);
}
