package com.tokopedia.session.changephonenumber.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.apiservices.accounts.UploadImageService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.changephonenumber.data.mapper.GeneratePostKeyMapper;
import com.tokopedia.session.changephonenumber.data.mapper.GetUploadHostMapper;
import com.tokopedia.session.changephonenumber.data.mapper.UploadImageMapper;
import com.tokopedia.session.changephonenumber.data.source.CloudGeneratePostKeySource;
import com.tokopedia.session.changephonenumber.data.source.CloudGetUploadHostSource;
import com.tokopedia.session.changephonenumber.data.source.CloudUploadImageDataSource;

/**
 * Created by nisie on 3/9/17.
 */

public class UploadImageSourceFactory {

    private Context context;
    private final AccountsService accountsService;
    private final UploadImageService uploadImageService;
    private GetUploadHostMapper getUploadHostMapper;
    private final GeneratePostKeyMapper generatePostKeyMapper;
    private final UploadImageMapper uploadImageMapper;

    public UploadImageSourceFactory(Context context,
                                    AccountsService accountsService,
                                    UploadImageService uploadImageService,
                                    GetUploadHostMapper getUploadHostMapper,
                                    GeneratePostKeyMapper generatePostKeyMapper,
                                    UploadImageMapper uploadImageMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.getUploadHostMapper = getUploadHostMapper;
        this.generatePostKeyMapper = generatePostKeyMapper;
        this.uploadImageService = uploadImageService;
        this.uploadImageMapper = uploadImageMapper;
    }

    public CloudGetUploadHostSource createCloudUploadHostDataStore() {
        return new CloudGetUploadHostSource(context, accountsService, getUploadHostMapper);
    }

    public CloudGeneratePostKeySource createCloudGeneratePostKeyDataStore() {
        return new CloudGeneratePostKeySource(context, accountsService, generatePostKeyMapper);
    }

    public CloudUploadImageDataSource createCloudUploadImageDataStore() {
        return new CloudUploadImageDataSource(context, uploadImageService, uploadImageMapper);
    }
}
