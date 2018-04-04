package com.tokopedia.inbox.inboxchat.uploadimage.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.inboxchat.uploadimage.data.factory.ImageUploadFactory;
import com.tokopedia.inbox.inboxchat.uploadimage.domain.model.GenerateHostDomain;
import com.tokopedia.inbox.inboxchat.uploadimage.domain.model.UploadImageDomain;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * @author by nisie on 9/5/17.
 */

public class ImageUploadRepositoryImpl implements ImageUploadRepository {

    ImageUploadFactory imageUploadFactory;

    public ImageUploadRepositoryImpl(ImageUploadFactory imageUploadFactory) {
        this.imageUploadFactory = imageUploadFactory;
    }

    @Override
    public Observable<GenerateHostDomain> generateHost(RequestParams parameters) {
        return imageUploadFactory
                .createCloudGenerateHostDataSource()
                .generateHost(parameters);
    }

    @Override
    public Observable<UploadImageDomain> uploadImage(String url,
                                                     Map<String, RequestBody> params,
                                                     RequestBody imageFile) {
        return imageUploadFactory
                .createCloudUploadImageDataSource()
                .uploadImage(url,
                        params,
                        imageFile);
    }
}
