package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.EditAppealSolutionResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.EditAppealResolutionSolutionDomain;
import com.tokopedia.inbox.rescenter.network.ResolutionResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 24/08/17.
 */

public class EditAppealResolutionResponseMapper
        implements Func1<Response<ResolutionResponse<EditAppealSolutionResponse>>, EditAppealResolutionSolutionDomain> {

    @Inject
    public EditAppealResolutionResponseMapper() {
    }

    @Override
    public EditAppealResolutionSolutionDomain call(Response<ResolutionResponse<EditAppealSolutionResponse>> response) {
        return mappingResponse(response);
    }

    private EditAppealResolutionSolutionDomain mappingResponse(Response<ResolutionResponse<EditAppealSolutionResponse>> response) {
        if (response.isSuccessful()) {
            if (response.body().isNullData()) {
                if (response.body().getErrorMessageJoined() != null || !response.body().getErrorMessageJoined().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException("");
                }
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        EditAppealSolutionResponse editAppealSolutionResponse =
                response.body().getData();
        EditAppealResolutionSolutionDomain model =
                new EditAppealResolutionSolutionDomain(response.isSuccessful(),
                        editAppealSolutionResponse != null ?
                                editAppealSolutionResponse.getSuccessMessage() :
                                null);
        model.setSuccess(true);
        return model;
    }
}
