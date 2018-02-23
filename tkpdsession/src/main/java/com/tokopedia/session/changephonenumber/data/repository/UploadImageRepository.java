package com.tokopedia.session.changephonenumber.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.changephonenumber.domain.model.changephonenumberrequest.SubmitImageModel;
import com.tokopedia.session.changephonenumber.domain.model.changephonenumberrequest.UploadHostModel;
import com.tokopedia.session.changephonenumber.domain.model.changephonenumberrequest.UploadImageModel;
import com.tokopedia.session.changephonenumber.domain.model.changephonenumberrequest.ValidateImageModel;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by nisie on 3/9/17.
 */

public interface UploadImageRepository {

    Observable<UploadImageModel> uploadImage(String url,
                                             Map<String, RequestBody> params,
                                             RequestBody imageFile);

    Observable<SubmitImageModel> submitImage(TKPDMapParam<String, Object> parameters);

    Observable<UploadHostModel> getUploadHost(TKPDMapParam<String, Object> parameters);

    Observable<ValidateImageModel> validateImage(TKPDMapParam<String, Object> parameters);
}
