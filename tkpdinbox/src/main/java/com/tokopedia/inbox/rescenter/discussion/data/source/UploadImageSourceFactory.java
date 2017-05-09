package com.tokopedia.inbox.rescenter.discussion.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResCenterActApi;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.apiservices.upload.apis.GeneratedHostActApi;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.CreatePictureMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.GenerateHostMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.SubmitImageMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.UploadImageMapper;

/**
 * Created by nisie on 4/3/17.
 */

public class UploadImageSourceFactory {

    private final Context context;
    private final GeneratedHostActApi generatedHostActApi;
    private final ResCenterActApi resCenterActApi;
    private final GenerateHostMapper generateHostMapper;
    private final UploadImageMapper uploadImageMapper;
    private final CreatePictureMapper createPictureMapper;
    private final SubmitImageMapper submitImageMapper;

    public UploadImageSourceFactory(Context context,
                                    GeneratedHostActApi generatedHostActApi,
                                    ResCenterActApi resCenterActApi,
                                    GenerateHostMapper generateHostMapper,
                                    UploadImageMapper uploadImageMapper,
                                    CreatePictureMapper createPictureMapper,
                                    SubmitImageMapper submitImageMapper) {
        this.context = context;
        this.generatedHostActApi = generatedHostActApi;
        this.resCenterActApi = resCenterActApi;

        this.generateHostMapper = generateHostMapper;
        this.uploadImageMapper = uploadImageMapper;
        this.createPictureMapper = createPictureMapper;
        this.submitImageMapper = submitImageMapper;
    }

    public CloudGenerateHostSource createCloudGenerateHostDataStore() {
        return new CloudGenerateHostSource(context, generatedHostActApi, generateHostMapper);
    }

    public CloudUploadImageDataSource createCloudUploadImageDataStore() {
        return new CloudUploadImageDataSource(context, resCenterActApi, uploadImageMapper);
    }

    public CloudCreatePictureDataSource createCloudCreatePictureDataStore() {
        return new CloudCreatePictureDataSource(context, resCenterActApi, createPictureMapper);
    }

    public CloudSubmitImageDataSource createCloudSubmitImageDataStore(){
        return new CloudSubmitImageDataSource(context, resCenterActApi, submitImageMapper);
    }
}
