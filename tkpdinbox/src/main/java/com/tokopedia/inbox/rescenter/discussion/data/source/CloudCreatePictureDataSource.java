package com.tokopedia.inbox.rescenter.discussion.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.upload.UploadImageService;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.UploadImageModel;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.CreatePictureMapper;
import com.tokopedia.inbox.rescenter.discussion.domain.model.CreatePictureModel;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by nisie on 4/3/17.
 */

public class CloudCreatePictureDataSource {

    private Context context;
    private final UploadImageService uploadImageService;
    private final CreatePictureMapper createPictureMapper;

    public CloudCreatePictureDataSource(Context context,
                                        UploadImageService uploadImageService,
                                        CreatePictureMapper createPictureMapper) {
        this.context = context;
        this.uploadImageService = uploadImageService;
        this.createPictureMapper = createPictureMapper;
    }

    public Observable<CreatePictureModel> createImage(String url,
                                                      Map<String, RequestBody> params) {
        return uploadImageService.getApi().createImage(
                url,
                params)
                .map(createPictureMapper);
    }
}
