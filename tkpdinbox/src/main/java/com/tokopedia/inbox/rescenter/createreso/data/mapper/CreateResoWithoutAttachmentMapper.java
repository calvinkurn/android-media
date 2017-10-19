package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.CreateResoWithoutAttachmentResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.ResolutionResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoWithoutAttachmentDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.ResolutionDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 05/09/17.
 */

public class CreateResoWithoutAttachmentMapper implements Func1<Response<TkpdResponse>, CreateResoWithoutAttachmentDomain> {

    private Context context;

    public CreateResoWithoutAttachmentMapper(Context context) {
        this.context = context;
    }

    @Override
    public CreateResoWithoutAttachmentDomain call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private CreateResoWithoutAttachmentDomain mappingResponse(Response<TkpdResponse> response) {
        CreateResoWithoutAttachmentResponse createResoWithoutAttachmentResponse =
                response.body().convertDataObj(CreateResoWithoutAttachmentResponse.class);
        CreateResoWithoutAttachmentDomain model = new CreateResoWithoutAttachmentDomain(
                createResoWithoutAttachmentResponse.getResolution() != null ?
                        mappingResolutionDomain(createResoWithoutAttachmentResponse.getResolution()) :
                        null,
                createResoWithoutAttachmentResponse.getCacheKey(),
                createResoWithoutAttachmentResponse.getSuccessMessage());
        if (response.isSuccessful()) {
            if (response.body().isNullData()) {
                if (response.body().getErrorMessageJoined() != null || !response.body().getErrorMessageJoined().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException(context.getResources().getString(R.string.string_general_error));
                }
            } else {
                model.setSuccess(true);
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return model;
    }

    private ResolutionDomain mappingResolutionDomain(ResolutionResponse response) {
        return new ResolutionDomain(response.getId());
    }
}
