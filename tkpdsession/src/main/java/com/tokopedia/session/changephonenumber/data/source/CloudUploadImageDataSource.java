package com.tokopedia.session.changephonenumber.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.apiservices.accounts.UploadImageService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.changephonenumber.data.UploadHostModel;
import com.tokopedia.session.changephonenumber.data.UploadImageModel;
import com.tokopedia.session.changephonenumber.data.mapper.GetUploadHostMapper;
import com.tokopedia.session.changephonenumber.data.mapper.UploadImageMapper;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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
