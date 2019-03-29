package com.tokopedia.inbox.rescenter.network;

import com.tokopedia.inbox.common.data.pojo.GenerateHostDataResponse;
import com.tokopedia.inbox.common.data.pojo.UploadResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.CreateResoWithoutAttachmentResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.CreateSubmitResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.CreateValidateResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem.ProductProblemListResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.EditAppealSolutionResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.EditAppealSolutionResponseResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.SolutionResponseResponse;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author by yfsx on 26/07/18.
 */
public interface ResolutionApi {

    String PATH_RESOLUTION_ID = "resolution_id";
    String PATH_ORDER_ID = "order_id";
    String PATH_TROUBLE_ID = "trouble_id";
    String PATH_CONVERSATION_ID = "conversation_id";

    //common section
    @GET(ResolutionUrl.PATH_GENERATE_TOKEN_HOST)
    Observable<Response<ResolutionResponse<GenerateHostDataResponse>>>
    generateHost(@QueryMap HashMap<String, Object> params);

    @Multipart
    @POST("")
    Observable<Response<ResolutionResponse<UploadResponse>>>
    uploadImage(@Url String url,
                @PartMap Map<String, RequestBody> params,
                @Part("fileToUpload\"; filename=\"image.jpg") RequestBody imageFile);

    @Multipart
    @POST("")
    Observable<Response<ResolutionResponse<UploadResponse>>>
    uploadVideo(@Url String url,
                @PartMap Map<String, RequestBody> params,
                @Part MultipartBody.Part file);

    //END of common section



    //create section
    @GET(ResolutionUrl.GET_RESOLUTION_STEP_1)
    Observable<Response<ResolutionResponse<ProductProblemListResponse>>>
    getProductProblemList(@Path(PATH_ORDER_ID) String orderId,
                          @QueryMap HashMap<String, Object> params);

    @POST(ResolutionUrl.POST_RESOLUTION_STEP_2_3)
    Observable<Response<ResolutionResponse<SolutionResponseResponse>>>
    getSolution(@Path(PATH_ORDER_ID) String orderId,
                @Body Object object);

    @POST(ResolutionUrl.BASE_RESOLUTION_CREATE)
    Observable<Response<ResolutionResponse<CreateResoWithoutAttachmentResponse>>>
    postCreateResolution(@Path(PATH_ORDER_ID) String orderId,
                         @Body Object object);

    @GET(ResolutionUrl.GET_RESOLUTION_RECOMPLAINT_STEP_1)
    Observable<Response<ResolutionResponse<ProductProblemListResponse>>>
    getProductProblemListRecomplaint(@Path(PATH_RESOLUTION_ID) String resolutionId,
                          @QueryMap HashMap<String, Object> params);

    @POST(ResolutionUrl.POST_RESOLUTION_RECOMPLAINT_STEP_2_3)
    Observable<Response<ResolutionResponse<SolutionResponseResponse>>>
    getSolutionRecomplaint(@Path(PATH_RESOLUTION_ID) String resolutionId,
                @Body Object object);

    @POST(ResolutionUrl.BASE_RESOLUTION_RECOMPLAINT)
    Observable<Response<ResolutionResponse<CreateResoWithoutAttachmentResponse>>>
    postCreateResolutionRecomplaint(@Path(PATH_RESOLUTION_ID) String resolutionId,
                         @Body Object object);

    @GET(ResolutionUrl.GET_RESOLUTION_EDIT)
    Observable<Response<ResolutionResponse<EditAppealSolutionResponseResponse>>>
    getEditSolution(@Path(PATH_RESOLUTION_ID) String resoId);


    @GET(ResolutionUrl.GET_RESOLUTION_APPEAL)
    Observable<Response<ResolutionResponse<EditAppealSolutionResponseResponse>>>
    getAppealSolution(@Path(PATH_RESOLUTION_ID) String resoId);

    @POST(ResolutionUrl.POST_RESOLUTION_EDIT)
    Observable<Response<ResolutionResponse<EditAppealSolutionResponse>>>
    postEditSolution(@Path(PATH_RESOLUTION_ID) String resoId,
                     @Body Object object);

    @POST(ResolutionUrl.POST_RESOLUTION_APPEAL)
    Observable<Response<ResolutionResponse<EditAppealSolutionResponse>>>
    postAppealSolution(@Path(PATH_RESOLUTION_ID) String resoId,
                       @Body Object object);


    @POST(ResolutionUrl.BASE_RESOLUTION_VALIDATE)
    Observable<Response<ResolutionResponse<CreateValidateResponse>>>
    postCreateValidateResolution(@Path(PATH_ORDER_ID) String orderId,
                                 @Body Object object);

    @POST(ResolutionUrl.BASE_RESOLUTION_SUBMIT)
    Observable<Response<ResolutionResponse<CreateSubmitResponse>>>
    postCreateSubmitResolution(@Path(PATH_ORDER_ID) String orderId,
                               @Body Object object);

    @POST(ResolutionUrl.BASE_RESOLUTION_RECOMPLAINT)
    Observable<Response<ResolutionResponse<CreateValidateResponse>>>
    postCreateValidateResolutionRecomplaint(@Path(PATH_RESOLUTION_ID) String resolutionId,
                                 @Body Object object);

    @POST(ResolutionUrl.BASE_RESOLUTION_RECOMPLAINT)
    Observable<Response<ResolutionResponse<CreateSubmitResponse>>>
    postCreateSubmitResolutionRecomplaint(@Path(PATH_RESOLUTION_ID) String resolutionId,
                               @Body Object object);

    //END of create section
}
