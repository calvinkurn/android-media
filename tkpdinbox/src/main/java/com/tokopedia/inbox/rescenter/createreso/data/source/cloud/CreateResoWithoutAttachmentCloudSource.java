package com.tokopedia.inbox.rescenter.createreso.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateResoWithoutAttachmentMapper;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoWithoutAttachmentDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.CreateResoWithoutAttachmentUseCase;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class CreateResoWithoutAttachmentCloudSource {
    private Context context;
    private CreateResoWithoutAttachmentMapper createResoWithoutAttachmentMapper;
    private ResolutionApi resolutionApi;

    public CreateResoWithoutAttachmentCloudSource(Context context,
                                                  CreateResoWithoutAttachmentMapper createResoWithoutAttachmentMapper,
                                                  ResolutionApi resolutionApi) {
        this.context = context;
        this.createResoWithoutAttachmentMapper = createResoWithoutAttachmentMapper;
        this.resolutionApi = resolutionApi;
    }

    public Observable<CreateResoWithoutAttachmentDomain> createResoWithoutAttachmentResponse(RequestParams requestParams) {
        try {
            return resolutionApi.postCreateResolution(requestParams.getString(
                    CreateResoWithoutAttachmentUseCase.ORDER_ID, ""),
                    requestParams.getString(CreateResoWithoutAttachmentUseCase.PARAM_RESULT, ""))
                    .map(createResoWithoutAttachmentMapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
