package com.tokopedia.inbox.rescenter.discussion.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.apis.ResCenterActApi;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.UploadImageModel;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.UploadVideoMapper;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by hangnadi on 6/2/17.
 */

public class CloudUploadVideoDataSource {

    private Context context;
    private final ResCenterActApi resCenterActApi;
    private final UploadVideoMapper uploadVideoMapper;

    public CloudUploadVideoDataSource(Context context,
                                      ResCenterActApi resCenterActApi,
                                      UploadVideoMapper uploadVideoMapper) {
        this.context = context;
        this.resCenterActApi = resCenterActApi;
        this.uploadVideoMapper = uploadVideoMapper;
    }

    public Observable<UploadImageModel> uploadVideo(String url,
                                                    Map<String, RequestBody> params,
                                                    MultipartBody.Part part) {
        return resCenterActApi.uploadVideo(url, params, part)
                .map(uploadVideoMapper);
    }
}
