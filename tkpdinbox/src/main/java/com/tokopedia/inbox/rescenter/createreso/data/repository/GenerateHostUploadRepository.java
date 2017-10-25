
package com.tokopedia.inbox.rescenter.createreso.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.GenerateHostDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.UploadDomain;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by yoasfs on 24/08/17.
 */

public interface GenerateHostUploadRepository {
    Observable<GenerateHostDomain> generateHost(TKPDMapParam<String, Object> param);

    Observable<UploadDomain> upload(String url, Map<String, RequestBody> requestBodyMap, RequestBody generateFile);

    Observable<UploadDomain> uploadVideo(String url, Map<String, RequestBody> requestBodyMap,
                                         MultipartBody.Part requestBody);
}
