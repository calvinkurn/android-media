package com.tokopedia.inbox.common.domain.usecase;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.inbox.common.data.source.ResolutionCommonSource;
import com.tokopedia.inbox.common.domain.model.UploadDomain;
import com.tokopedia.usecase.RequestParams;

import java.io.File;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * @author by yfsx on 30/07/18.
 */

public class UploadVideoUseCase extends UploadImageUseCase {

    @Inject
    public UploadVideoUseCase(ResolutionCommonSource resolutionCommonSource) {
        super(resolutionCommonSource);
    }

    @Override
    public Observable<UploadDomain> createObservable(RequestParams requestParams) {
        return resolutionCommonSource.uploadVideo(generateUrl(requestParams),
                generateRequestBody(requestParams),
                getVideoFile(requestParams)
        );
    }

    @Override
    protected String generateUrl(RequestParams requestParams) {
        return HTTP
                + requestParams.getString(PARAM_GENERATED_HOST, "")
                + TkpdBaseURL.Upload.PATH_UPLOAD_VIDEO;
    }

    private MultipartBody.Part getVideoFile(RequestParams requestParams) {
        File file = new File(requestParams.getString(PARAM_FILE_TO_UPLOAD, ""));
        RequestBody requestBody = RequestBody.create(MediaType.parse("video/mp4"), file);
        return MultipartBody.Part.createFormData("fileToUpload", file.getName(), requestBody);
    }

}
