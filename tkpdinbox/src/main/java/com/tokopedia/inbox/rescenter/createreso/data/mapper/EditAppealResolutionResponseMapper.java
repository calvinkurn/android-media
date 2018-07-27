package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.EditAppealSolutionResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.EditAppealResolutionSolutionDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 24/08/17.
 */

public class EditAppealResolutionResponseMapper
        implements Func1<Response<DataResponse<EditAppealSolutionResponse>>, EditAppealResolutionSolutionDomain> {

    @Inject
    public EditAppealResolutionResponseMapper() {
    }

    @Override
    public EditAppealResolutionSolutionDomain call(Response<DataResponse<EditAppealSolutionResponse>> response) {
        return mappingResponse(response);
    }

    private EditAppealResolutionSolutionDomain mappingResponse(Response<DataResponse<EditAppealSolutionResponse>> response) {

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
