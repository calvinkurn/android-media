package com.tokopedia.inbox.inboxchat.uploadimage.domain.interactor;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.uploadimage.data.repository.ImageUploadRepository;
import com.tokopedia.inbox.inboxchat.uploadimage.domain.model.UploadImageDomain;
import com.tokopedia.inbox.inboxchat.util.ImageUploadHandlerChat;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * @author by nisie on 9/5/17.
 */

public class UploadImageUseCase extends UseCase<UploadImageDomain> {

    public static final String PARAM_GENERATED_HOST = "PARAM_GENERATED_HOST";
    public static final String PARAM_FILE_TO_UPLOAD = "PARAM_FILE_TO_UPLOAD";

    public static final String PARAM_IMAGE_ID = "id";
    private static final String PARAM_WEB_SERVICE = "web_service";

    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_DEVICE_ID = "device_id";
    private static final String PARAM_HASH = "hash";
    private static final String PARAM_OS_TYPE = "os_type";
    private static final String PARAM_TIMESTAMP = "device_time";

    public static final String PARAM_SERVER_ID = "server_id";
    public static final String PARAM_TOKEN = "token";

    private static final String HTTP = "http://";
    private static final String UPLOAD_ATTACHMENT = "/upload/attachment";

    private ImageUploadRepository imageUploadRepository;

    public UploadImageUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              ImageUploadRepository imageUploadRepository) {
        super(threadExecutor, postExecutionThread);
        this.imageUploadRepository = imageUploadRepository;
    }

    @Override
    public Observable<UploadImageDomain> createObservable(RequestParams requestParams) {
        return imageUploadRepository.uploadImage(generateUrl(requestParams),
                generateRequestBody(requestParams),
                generateImage(requestParams)
        );
    }

    private String generateUrl(RequestParams requestParams) {
        return HTTP
                + requestParams.getString(PARAM_GENERATED_HOST, "")
                + UPLOAD_ATTACHMENT;
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
        RequestBody imageId = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(PARAM_IMAGE_ID, ""));
        RequestBody webservice = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(PARAM_WEB_SERVICE, "1"
                ));
        RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"),
                String.valueOf(requestParams.getString(PARAM_SERVER_ID, "")));

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put(PARAM_USER_ID, userId);
        requestBodyMap.put(PARAM_DEVICE_ID, deviceId);
        requestBodyMap.put(PARAM_OS_TYPE, osType);
        requestBodyMap.put(PARAM_HASH, hash);
        requestBodyMap.put(PARAM_TIMESTAMP, deviceTime);
        requestBodyMap.put(PARAM_IMAGE_ID, imageId);
        requestBodyMap.put(PARAM_WEB_SERVICE, webservice);
        requestBodyMap.put(PARAM_SERVER_ID, serverId);

        return requestBodyMap;
    }

    private RequestBody generateImage(RequestParams requestParams) {
        File file = null;
        try {
            byte[] temp = ImageUploadHandlerChat.compressImage(requestParams.getString(PARAM_FILE_TO_UPLOAD, ""));
            file = ImageUploadHandlerChat.writeImageToTkpdPath(temp);
            if(ImageUploadHandlerChat.checkSizeOverLimit(file, 5)){
                throw new ErrorMessageException("Gambar melebihi 5MB");
            }
        } catch (IOException e) {
            throw new RuntimeException(MainApplication.getAppContext().getString(R.string.error_upload_image));
        }
        return RequestBody.create(MediaType.parse("image/*"),
                file);
    }

    public static RequestParams getParam(RequestParams requestParams, String uploadHost,
                                         String imageId, String fileLoc, String serverId) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_GENERATED_HOST, uploadHost);
        params.putString(PARAM_FILE_TO_UPLOAD, fileLoc);
        params.putString(PARAM_SERVER_ID, serverId);
        String userId = requestParams.getString(PARAM_USER_ID,
                SessionHandler.getTempLoginSession(MainApplication.getAppContext()));
        params.putString(UploadImageUseCase.PARAM_USER_ID, userId);
        params.putString(UploadImageUseCase.PARAM_IMAGE_ID, String.format("%s%s", userId, imageId));
        params.putString(UploadImageUseCase.PARAM_DEVICE_ID,
                requestParams.getString(PARAM_DEVICE_ID,
                        GCMHandler.getRegistrationId(MainApplication.getAppContext())));
        params.putString(UploadImageUseCase.PARAM_WEB_SERVICE, "1");
        params.putString(UploadImageUseCase.PARAM_IMAGE_ID, imageId);
        return params;
    }
}
