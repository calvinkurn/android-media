package com.tokopedia.inbox.rescenter.discussion.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.detailv2.domain.UploadImageRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.UploadImageModel;
import com.tokopedia.inbox.rescenter.discussion.data.source.UploadImageSourceFactory;
import com.tokopedia.inbox.rescenter.discussion.domain.model.CreatePictureModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replysubmit.ReplySubmitModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.generatehost.GenerateHostModel;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by nisie on 4/3/17.
 */

public class UploadImageRepositoryImpl implements UploadImageRepository {

    private final UploadImageSourceFactory uploadImageSourceFactory;

    public UploadImageRepositoryImpl(UploadImageSourceFactory uploadImageSourceFactory) {
        this.uploadImageSourceFactory = uploadImageSourceFactory;
    }

    @Override
    public Observable<GenerateHostModel> generateHost(TKPDMapParam<String, Object> parameters) {
        return uploadImageSourceFactory
                .createCloudGenerateHostDataStore()
                .generateHost(parameters);
    }

    @Override
    public Observable<ReplySubmitModel> submitImage(TKPDMapParam<String, Object> parameters) {
        return uploadImageSourceFactory
                .createCloudSubmitImageDataStore()
                .submitImage(
                        parameters
                        );
    }

    @Override
    public Observable<UploadImageModel> uploadImage(String url,
                                                    Map<String, RequestBody> params,
                                                    RequestBody imageFile) {
        return uploadImageSourceFactory
                .createCloudUploadImageDataStore()
                .uploadImage(
                        url,
                        params,
                        imageFile);
    }

    @Override
    public Observable<CreatePictureModel> createImageResCenter(String url,
                                                               Map<String, RequestBody> params) {
        return uploadImageSourceFactory
                .createCloudCreatePictureDataStore()
                .createImage(
                        url,
                        params);
    }
}
