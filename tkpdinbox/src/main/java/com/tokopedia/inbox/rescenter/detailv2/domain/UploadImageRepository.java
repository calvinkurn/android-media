package com.tokopedia.inbox.rescenter.detailv2.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.UploadImageModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.CreatePictureModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replysubmit.ReplySubmitModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.generatehost.GenerateHostModel;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by nisie on 4/3/17.
 */

public interface UploadImageRepository {

    Observable<GenerateHostModel> generateHost(TKPDMapParam<String, Object> parameters);

    Observable<ReplySubmitModel> submitImage(TKPDMapParam<String, Object> parameters);

    Observable<UploadImageModel> uploadImage(String url,
                                             Map<String, RequestBody> params,
                                             RequestBody imageFile);

    Observable<CreatePictureModel> createImageResCenter(String url,
                                                        Map<String, RequestBody> stringRequestBodyMap);
}
