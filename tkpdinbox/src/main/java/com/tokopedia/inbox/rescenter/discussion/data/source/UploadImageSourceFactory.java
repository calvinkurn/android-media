package com.tokopedia.inbox.rescenter.discussion.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.CreatePictureMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.GenerateHostMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.SubmitImageMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.UploadImageMapper;

/**
 * Created by nisie on 4/3/17.
 */

public class UploadImageSourceFactory {

    private final Context context;
    private final GenerateHostActService generateHostActService;
    private final ResCenterActService resCenterActService;
    private final GenerateHostMapper generateHostMapper;
    private final UploadImageMapper uploadImageMapper;
    private final CreatePictureMapper createPictureMapper;
    private final SubmitImageMapper submitImageMapper;

    public UploadImageSourceFactory(Context context,
                                    GenerateHostActService generateHostActService,
                                    ResCenterActService resCenterActService,
                                    GenerateHostMapper generateHostMapper,
                                    UploadImageMapper uploadImageMapper,
                                    CreatePictureMapper createPictureMapper,
                                    SubmitImageMapper submitImageMapper) {
        this.context = context;
        this.generateHostActService = generateHostActService;
        this.resCenterActService = resCenterActService;

        this.generateHostMapper = generateHostMapper;
        this.uploadImageMapper = uploadImageMapper;
        this.createPictureMapper = createPictureMapper;
        this.submitImageMapper = submitImageMapper;
    }

    public CloudGenerateHostSource createCloudGenerateHostDataStore() {
        return new CloudGenerateHostSource(context, generateHostActService, generateHostMapper);
    }

    public CloudUploadImageDataSource createCloudUploadImageDataStore() {
        return new CloudUploadImageDataSource(context, resCenterActService, uploadImageMapper);
    }

    public CloudCreatePictureDataSource createCloudCreatePictureDataStore() {
        return new CloudCreatePictureDataSource(context, resCenterActService, createPictureMapper);
    }

    public CloudSubmitImageDataSource createCloudSubmitImageDataStore(){
        return new CloudSubmitImageDataSource(context, resCenterActService, submitImageMapper);
    }
}
