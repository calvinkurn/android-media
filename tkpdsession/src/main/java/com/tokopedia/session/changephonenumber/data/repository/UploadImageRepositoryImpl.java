package com.tokopedia.session.changephonenumber.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.changephonenumber.data.ChangePhoneNumberModel;
import com.tokopedia.session.changephonenumber.data.SubmitImageModel;
import com.tokopedia.session.changephonenumber.data.ValidateImageModel;
import com.tokopedia.session.changephonenumber.data.UploadHostModel;
import com.tokopedia.session.changephonenumber.data.UploadImageModel;
import com.tokopedia.session.changephonenumber.data.factory.UploadImageSourceFactory;
import com.tokopedia.session.changephonenumber.domain.UploadImageRepository;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by nisie on 3/9/17.
 */

public class UploadImageRepositoryImpl implements UploadImageRepository {

    private final UploadImageSourceFactory uploadImageSourceFactory;

    public UploadImageRepositoryImpl(UploadImageSourceFactory uploadImageSourceFactory) {

        this.uploadImageSourceFactory = uploadImageSourceFactory;
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
    public Observable<SubmitImageModel> submitImage(TKPDMapParam<String, Object> parameters) {
        return uploadImageSourceFactory
                .createCloudSubmitImageDataStore()
                .submitImage(
                        parameters);
    }

    @Override
    public Observable<UploadHostModel> getUploadHost(TKPDMapParam<String, Object> parameters) {
        return uploadImageSourceFactory
                .createCloudUploadHostDataStore()
                .getUploadHost(parameters);
    }

    @Override
    public Observable<ValidateImageModel> validateImage(TKPDMapParam<String, Object> parameters) {
        return uploadImageSourceFactory
                .createCloudValidateImageDataStore()
                .validateImage(parameters);
    }
}
