package com.tokopedia.session.changephonenumber.data.source.changephonenumberrequest;

import android.content.Context;

import com.tokopedia.network.service.UploadImageService;
import com.tokopedia.session.changephonenumber.domain.model.changephonenumberrequest.UploadImageModel;
import com.tokopedia.session.changephonenumber.data.mapper.changephonenumberrequest.UploadImageMapper;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by nisie on 3/10/17.
 */

public class CloudUploadImageDataSource {
    private Context context;
    private final UploadImageService uploadImageService;
    private UploadImageMapper uploadImageMapper;

    public CloudUploadImageDataSource(Context context,
                                      UploadImageService uploadImageService,
                                      UploadImageMapper uploadImageMapper) {
        this.context = context;
        this.uploadImageService = uploadImageService;
        this.uploadImageMapper = uploadImageMapper;
    }

    public Observable<UploadImageModel> uploadImage(String url,
                                                    Map<String, RequestBody> params,
                                                    RequestBody imageFile) {
        return uploadImageService.getApi().uploadImage(
                url,
                params,
                imageFile)
                .map(uploadImageMapper);
    }
}
