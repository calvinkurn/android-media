package com.tokopedia.inbox.rescenter.discussion.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.apis.ResCenterActApi;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.UploadImageModel;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.UploadImageMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.UploadImageV2Mapper;

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
    private final UploadImageV2Mapper uploadImageV2Mapper;

    public CloudUploadImageDataSource(Context context,
                                      ResCenterActApi resCenterActApi,
                                      UploadImageMapper uploadImageMapper,
                                      UploadImageV2Mapper uploadImageV2Mapper) {
        this.context = context;
        this.resCenterActApi = resCenterActApi;
        this.uploadImageMapper = uploadImageMapper;
        this.uploadImageV2Mapper = uploadImageV2Mapper;
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


    public Observable<UploadImageModel> newUploadImage(String url,
                                                       Map<String, RequestBody> params,
                                                       RequestBody imageFile) {
        return resCenterActApi.uploadImage(
                url,
                params,
                imageFile)
                .map(uploadImageV2Mapper);
    }
}
