package com.tokopedia.otp.securityquestion.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.otp.securityquestion.data.model.changephonenumberrequest.SubmitImageModel;
import com.tokopedia.otp.securityquestion.data.model.changephonenumberrequest.ValidateImageModel;
import com.tokopedia.otp.securityquestion.data.model.changephonenumberrequest.UploadHostModel;
import com.tokopedia.otp.securityquestion.data.model.changephonenumberrequest.UploadImageModel;
import com.tokopedia.otp.securityquestion.data.factory.UploadImageSourceFactory;

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
