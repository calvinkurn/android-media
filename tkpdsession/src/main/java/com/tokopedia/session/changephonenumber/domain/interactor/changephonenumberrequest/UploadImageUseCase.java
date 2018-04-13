package com.tokopedia.session.changephonenumber.domain.interactor.changephonenumberrequest;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.domain.model.changephonenumberrequest.UploadImageModel;
import com.tokopedia.session.changephonenumber.data.repository.UploadImageRepository;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by nisie on 3/10/17.
 */

public class UploadImageUseCase extends UseCase<UploadImageModel> {

    public static final String PARAM_URL = "url";
    public static final String PARAM_SERVER_ID = "server_id";

    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_DEVICE_ID = "device_id";
    private static final String PARAM_HASH = "hash";
    private static final String PARAM_OS_TYPE = "os_type";
    private static final String PARAM_TIMESTAMP = "device_time";

    private static final String NEW_ADD = "1";
    private static final String PARAM_NEW_ADD = "new_add";
    public static final String PARAM_FILE_TO_UPLOAD = "fileToUpload";
    public static final String PARAM_IMAGE_ID = "id";
    public static final String PARAM_TOKEN = "token";
    public static final String PARAM_WEB_SERVICE = "web_service";


    private final UploadImageRepository uploadImageRepository;

    public UploadImageUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              UploadImageRepository uploadImageRepository) {
        super(threadExecutor, postExecutionThread);
        this.uploadImageRepository = uploadImageRepository;
    }

    @Override
    public Observable<UploadImageModel> createObservable(RequestParams requestParams) {
        String url = requestParams.getString(PARAM_URL, "") + "/upload/attachment";

        return uploadImageRepository.uploadImage(url,
                generateRequestBody(requestParams),
                getUploadImageFile(requestParams)
        );
    }

    private RequestBody getUploadImageFile(RequestParams requestParams) {
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

    private Map<String, RequestBody> generateRequestBody(RequestParams requestParams) {
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
        RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"),
                String.valueOf(requestParams.getString(PARAM_SERVER_ID, "")));
        RequestBody newAdd = RequestBody.create(MediaType.parse("text/plain"),
                NEW_ADD);
        RequestBody imageId = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(PARAM_IMAGE_ID, ""));
        RequestBody token = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(PARAM_TOKEN, ""
                ));
        RequestBody webservice = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(PARAM_WEB_SERVICE, "1"
                ));

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put(PARAM_USER_ID, userId);
        requestBodyMap.put(PARAM_DEVICE_ID, deviceId);
        requestBodyMap.put(PARAM_OS_TYPE, osType);
        requestBodyMap.put(PARAM_HASH, hash);
        requestBodyMap.put(PARAM_TIMESTAMP, deviceTime);
        requestBodyMap.put(PARAM_NEW_ADD, newAdd);
        requestBodyMap.put(PARAM_SERVER_ID, serverId);
        requestBodyMap.put(PARAM_IMAGE_ID, imageId);
        requestBodyMap.put(PARAM_FILE_TO_UPLOAD, imageId);
        requestBodyMap.put(PARAM_TOKEN, token);
        requestBodyMap.put(PARAM_WEB_SERVICE, webservice);

        return requestBodyMap;
    }
}
