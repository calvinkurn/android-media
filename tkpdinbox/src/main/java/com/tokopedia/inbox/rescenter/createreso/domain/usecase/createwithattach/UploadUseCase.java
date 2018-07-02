package com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.data.repository.GenerateHostUploadRepository;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoRequestDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.UploadDomain;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by yoasfs on 12/09/17.
 */

public class UploadUseCase extends UseCase<UploadDomain> {

    public static final String PARAM_GENERATED_HOST = "PARAM_GENERATED_HOST";
    public static final String PARAM_FILE_TO_UPLOAD = "PARAM_FILE_TO_UPLOAD";

    public static final String PARAM_IMAGE_ID = "id";
    private static final String PARAM_WEB_SERVICE = "web_service";

    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_DEVICE_ID = "device_id";
    private static final String PARAM_HASH = "hash";
    private static final String PARAM_OS_TYPE = "os_type";
    private static final String PARAM_TIMESTAMP = "device_time";

    protected static final String HTTP = "http://";
    private static final String UPLOAD_ATTACHMENT = "/upload/attachment/";

    protected GenerateHostUploadRepository generateHostUploadRepository;

    public UploadUseCase(ThreadExecutor threadExecutor,
                         PostExecutionThread postExecutionThread,
                         GenerateHostUploadRepository generateHostUploadRepository) {
        super(threadExecutor, postExecutionThread);
        this.generateHostUploadRepository = generateHostUploadRepository;
    }

    @Override
    public Observable<UploadDomain> createObservable(RequestParams requestParams) {
        return generateHostUploadRepository.upload(generateUrl(requestParams),
                generateRequestBody(requestParams),
                generateImage(requestParams)
        );
    }

    protected String generateUrl(RequestParams requestParams) {
        return HTTP
                + requestParams.getString(PARAM_GENERATED_HOST, "")
                + UPLOAD_ATTACHMENT;
    }

    protected Map<String, RequestBody> generateRequestBody(RequestParams requestParams) {
        Map<String, String> paramsMap = AuthUtil.generateParams(
                requestParams.getString(PARAM_USER_ID,
                        SessionHandler.getLoginID(MainApplication.getAppContext())),
                requestParams.getString(PARAM_DEVICE_ID,
                        GCMHandler.getRegistrationId(MainApplication.getAppContext())),
                new HashMap<String, String>()
        );

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(PARAM_USER_ID,
                        paramsMap.get(PARAM_USER_ID)));
        RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(PARAM_DEVICE_ID,
                        paramsMap.get(PARAM_DEVICE_ID)));
        RequestBody osType = RequestBody.create(MediaType.parse("text/plain"),
                paramsMap.get(PARAM_OS_TYPE));

        RequestBody hash = RequestBody.create(MediaType.parse("text/plain"),
                paramsMap.get(PARAM_HASH));
        RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"),
                paramsMap.get(PARAM_TIMESTAMP));
        RequestBody imageId = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(PARAM_IMAGE_ID, ""));
        RequestBody webservice = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(PARAM_WEB_SERVICE, "1"
                ));

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put(PARAM_USER_ID, userId);
        requestBodyMap.put(PARAM_DEVICE_ID, deviceId);
        requestBodyMap.put(PARAM_OS_TYPE, osType);
        requestBodyMap.put(PARAM_HASH, hash);
        requestBodyMap.put(PARAM_TIMESTAMP, deviceTime);
        requestBodyMap.put(PARAM_IMAGE_ID, imageId);
        requestBodyMap.put(PARAM_WEB_SERVICE, webservice);
        return requestBodyMap;
    }

    private RequestBody generateImage(RequestParams requestParams) {
        File file = null;
        try {
            file = ImageUploadHandler.writeImageToTkpdPath(
                    ImageUploadHandler.compressImage(requestParams.getString(PARAM_FILE_TO_UPLOAD, ""))
            );
        } catch (IOException e) {
            throw new RuntimeException(MainApplication.getAppContext().getString(R.string.error_upload_image));
        }
        return RequestBody.create(MediaType.parse("image/*"),
                file);
    }

    public static RequestParams getParam(CreateResoRequestDomain createResoRequestDomain,
                                         String imageId, String fileLoc) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_GENERATED_HOST,
                createResoRequestDomain.getGenerateHostDomain().getUploadHost());
        params.putString(UploadUseCase.PARAM_IMAGE_ID,
                imageId);
        params.putString(UploadUseCase.PARAM_FILE_TO_UPLOAD,
                fileLoc);
        return params;
    }
}
