package com.tokopedia.inbox.rescenter.network;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.CreateResoWithoutAttachmentResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem.ProductProblemListResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.AppealSolutionResponseResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.EditAppealSolutionResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.EditSolutionResponseResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.SolutionResponseResponse;

import java.util.HashMap;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    Observable<Response<DataResponse<ProductProblemListResponse>>>
    getProductProblemList(@Path(PATH_ORDER_ID) String orderId,
                          @QueryMap HashMap<String, Object> params);

    @FormUrlEncoded
    @POST(ResolutionUrl.POST_RESOLUTION_STEP_2_3)
    Observable<Response<DataResponse<SolutionResponseResponse>>>
    getSolution(@Path(PATH_ORDER_ID) String orderId,
                @FieldMap HashMap<String, Object> params);

    @POST(ResolutionUrl.BASE_RESOLUTION_CREATE)
    Observable<Response<DataResponse<CreateResoWithoutAttachmentResponse>>>
    postCreateResolution(@Path(PATH_ORDER_ID) String orderId,
                         @Body String object);

    @GET(ResolutionUrl.GET_RESOLUTION_EDIT)
    Observable<Response<DataResponse<EditSolutionResponseResponse>>>
    getEditSolution(@Path(PATH_RESOLUTION_ID) String resoId);


    @GET(ResolutionUrl.GET_RESOLUTION_APPEAL)
    Observable<Response<DataResponse<AppealSolutionResponseResponse>>>
    getAppealSolution(@Path(PATH_RESOLUTION_ID) String resoId);


    @FormUrlEncoded
    @POST(ResolutionUrl.POST_RESOLUTION_EDIT)
    Observable<Response<DataResponse<EditAppealSolutionResponse>>>
    postEditSolution(@Path(PATH_RESOLUTION_ID) String resoId,
                     @FieldMap HashMap<String, Object> params);


    @FormUrlEncoded
    @POST(ResolutionUrl.POST_RESOLUTION_APPEAL)
    Observable<Response<DataResponse<EditAppealSolutionResponse>>>
    postAppealSolution(@Path(PATH_RESOLUTION_ID) String resoId,
                       @FieldMap HashMap<String, Object> params);

}
