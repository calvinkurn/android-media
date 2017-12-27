package com.tokopedia.inbox.rescenter.discussion.domain.interactor;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.rescenter.detailv2.domain.UploadImageRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.UploadImageModel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by hangnadi on 6/2/17.
 */

public class UploadVideoUseCase extends UseCase<UploadImageModel> {

    public static final String PARAM_URL = "url";

    public static final String PARAM_DEVICE_ID = "device_id";
    private static final String PARAM_HASH = "hash";
    private static final String PARAM_OS_TYPE = "os_type";
    private static final String PARAM_TIMESTAMP = "device_time";

    public static final String PARAM_FILE_NAME = "name";
    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_FILE_ID = "id";
    public static final String PARAM_TOKEN = "token";
    public static final String PARAM_SERVER_ID = "server_id";
    public static final String PARAM_FILE_TO_UPLOAD = "fileToUpload";
    public static final String PARAM_WEB_SERVICE = "web_service";

    private final UploadImageRepository uploadImageRepository;

    public UploadVideoUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              UploadImageRepository uploadImageRepository) {
        super(threadExecutor, postExecutionThread);
        this.uploadImageRepository = uploadImageRepository;
    }

    @Override
    public Observable<UploadImageModel> createObservable(RequestParams requestParams) {
        String url =  "https://" + requestParams.getString(PARAM_URL, "") + TkpdBaseURL.Upload.PATH_UPLOAD_VIDEO;
        return uploadImageRepository.uploadVideo(url, generateParams(requestParams), getVideoFile(requestParams));
    }

    private MultipartBody.Part getVideoFile(RequestParams requestParams) {
        File file = new File(requestParams.getString(PARAM_FILE_TO_UPLOAD, ""));
        RequestBody requestBody = RequestBody.create(MediaType.parse("video/mp4"), file);

        return MultipartBody.Part.createFormData("fileToUpload", file.getName(), requestBody);
    }

    private Map<String, RequestBody> generateParams(RequestParams requestParams) {
        Map<String, String> paramsMap = AuthUtil.generateParams(
                requestParams.getString(PARAM_USER_ID,
                        SessionHandler.getLoginID(MainApplication.getAppContext())),
                requestParams.getString(PARAM_DEVICE_ID,
                        GCMHandler.getRegistrationId(MainApplication.getAppContext())),
                new HashMap<String, String>()
        );

        RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(PARAM_DEVICE_ID, paramsMap.get(PARAM_DEVICE_ID)));

        RequestBody osType = RequestBody.create(MediaType.parse("text/plain"),
                paramsMap.get(PARAM_OS_TYPE));
        RequestBody hash = RequestBody.create(MediaType.parse("text/plain"),
                paramsMap.get(PARAM_HASH));
        RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"),
                paramsMap.get(PARAM_TIMESTAMP));

        RequestBody fileName = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(PARAM_FILE_NAME, ""));

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(PARAM_USER_ID, paramsMap.get(PARAM_USER_ID)));

        RequestBody fileId = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(PARAM_FILE_ID,
                        SessionHandler.getLoginID(MainApplication.getAppContext()))
        );

        RequestBody token = RequestBody.create(MediaType.parse("text/plain"),
                SessionHandler.getAccessToken()
        );

        RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"),
                String.valueOf(requestParams.getString(PARAM_SERVER_ID, "")));

        RequestBody webservice = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(PARAM_WEB_SERVICE, "1")
        );

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put(PARAM_USER_ID, userId);
        requestBodyMap.put(PARAM_DEVICE_ID, deviceId);
        requestBodyMap.put(PARAM_OS_TYPE, osType);
        requestBodyMap.put(PARAM_HASH, hash);
        requestBodyMap.put(PARAM_TIMESTAMP, deviceTime);
        requestBodyMap.put(PARAM_FILE_NAME, fileName);
        requestBodyMap.put(PARAM_SERVER_ID, serverId);
        requestBodyMap.put(PARAM_FILE_ID, fileId);
        requestBodyMap.put(PARAM_TOKEN, token);
        requestBodyMap.put(PARAM_WEB_SERVICE, webservice);
        /*requestBodyMap.put(
                //"fileToUpload\"; name=\"" + requestParams.getString(PARAM_FILE_NAME, "") + "\"; type=\"video/mp4\"",
                "fileToUpload\"; type=\"video/mp4",
                fileBody
        );*/

        return requestBodyMap;
    }
}
