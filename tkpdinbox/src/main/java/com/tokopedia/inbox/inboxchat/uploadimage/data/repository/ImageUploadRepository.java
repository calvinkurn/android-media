package com.tokopedia.inbox.inboxchat.uploadimage.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.inboxchat.uploadimage.domain.model.GenerateHostDomain;
import com.tokopedia.inbox.inboxchat.uploadimage.domain.model.UploadImageDomain;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * @author by nisie on 9/5/17.
 */

public interface ImageUploadRepository {

    Observable<GenerateHostDomain> generateHost(RequestParams parameters);

    Observable<UploadImageDomain> uploadImage(String url,
                                              Map<String, RequestBody> params,
                                              RequestBody imageFile);
}
