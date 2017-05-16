package com.tokopedia.inbox.rescenter.discussion.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.apis.ResCenterActApi;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.UploadImageModel;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.UploadImageMapper;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by nisie on 4/3/17.
 */

public class CloudUploadImageDataSource {

    private Context context;
    private final ResCenterActApi resCenterActApi;
    private final UploadImageMapper uploadImageMapper;

    public CloudUploadImageDataSource(Context context,
                                      ResCenterActApi resCenterActApi,
                                      UploadImageMapper uploadImageMapper) {
        this.context = context;
        this.resCenterActApi = resCenterActApi;
        this.uploadImageMapper = uploadImageMapper;
    }

    public Observable<UploadImageModel> uploadImage(String url,
                                                    Map<String, RequestBody> params,
                                                    RequestBody imageFile) {
        return resCenterActApi.uploadImage(
                url,
                params,
                imageFile)
                .map(uploadImageMapper);
    }


}
