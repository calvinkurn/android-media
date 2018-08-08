package com.tokopedia.session.changephonenumber.data.factory;

import android.content.Context;

import com.tokopedia.network.service.AccountsService;
import com.tokopedia.network.service.UploadImageService;
import com.tokopedia.session.changephonenumber.data.mapper.changephonenumberrequest.SubmitImageMapper;
import com.tokopedia.session.changephonenumber.data.mapper.changephonenumberrequest.ValidateImageMapper;
import com.tokopedia.session.changephonenumber.data.mapper.changephonenumberrequest.GetUploadHostMapper;
import com.tokopedia.session.changephonenumber.data.mapper.changephonenumberrequest.UploadImageMapper;
import com.tokopedia.session.changephonenumber.data.source.changephonenumberrequest.CloudSubmitImageDataSource;
import com.tokopedia.session.changephonenumber.data.source.changephonenumberrequest.CloudValidateImageSource;
import com.tokopedia.session.changephonenumber.data.source.changephonenumberrequest.CloudGetUploadHostSource;
import com.tokopedia.session.changephonenumber.data.source.changephonenumberrequest.CloudUploadImageDataSource;

/**
 * Created by nisie on 3/9/17.
 */

public class UploadImageSourceFactory {

    private Context context;
    private final AccountsService accountsService;
    private final UploadImageService uploadImageService;
    private GetUploadHostMapper getUploadHostMapper;
    private final ValidateImageMapper validateImageMapper;
    private final UploadImageMapper uploadImageMapper;
    private final SubmitImageMapper submitImageMapper;

    public UploadImageSourceFactory(Context context,
                                    AccountsService accountsService,
                                    UploadImageService uploadImageService,
                                    GetUploadHostMapper getUploadHostMapper,
                                    ValidateImageMapper validateImageMapper,
                                    UploadImageMapper uploadImageMapper,
                                    SubmitImageMapper submitImageMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.getUploadHostMapper = getUploadHostMapper;
        this.validateImageMapper = validateImageMapper;
        this.uploadImageService = uploadImageService;
        this.uploadImageMapper = uploadImageMapper;
        this.submitImageMapper = submitImageMapper;
    }

    public CloudGetUploadHostSource createCloudUploadHostDataStore() {
        return new CloudGetUploadHostSource(context, accountsService, getUploadHostMapper);
    }

    public CloudValidateImageSource createCloudValidateImageDataStore() {
        return new CloudValidateImageSource(context, accountsService, validateImageMapper);
    }

    public CloudSubmitImageDataSource createCloudSubmitImageDataStore() {
        return new CloudSubmitImageDataSource(context, accountsService, submitImageMapper);
    }

    public CloudUploadImageDataSource createCloudUploadImageDataStore() {
        return new CloudUploadImageDataSource(context, uploadImageService, uploadImageMapper);
    }
}
