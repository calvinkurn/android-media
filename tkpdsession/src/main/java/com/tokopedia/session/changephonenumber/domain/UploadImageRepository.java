package com.tokopedia.session.changephonenumber.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.changephonenumber.data.ChangePhoneNumberModel;
import com.tokopedia.session.changephonenumber.data.GeneratePostKeyModel;
import com.tokopedia.session.changephonenumber.data.UploadHostModel;
import com.tokopedia.session.changephonenumber.data.UploadImageModel;

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

    Observable<ChangePhoneNumberModel> submitRequest(TKPDMapParam<String, Object> parameters);

    Observable<UploadHostModel> getUploadHost(TKPDMapParam<String, Object> parameters);

    Observable<GeneratePostKeyModel> generatePostKey(TKPDMapParam<String, Object> parameters);
}
