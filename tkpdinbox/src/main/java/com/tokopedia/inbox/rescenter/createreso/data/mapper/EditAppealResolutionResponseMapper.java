package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import android.content.Context;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.EditAppealSolutionResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.EditAppealResolutionSolutionDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 24/08/17.
 */

public class EditAppealResolutionResponseMapper
        implements Func1<Response<TkpdResponse>, EditAppealResolutionSolutionDomain> {

    private Context context;

    public EditAppealResolutionResponseMapper(Context context) {
        this.context = context;
    }

    @Override
    public EditAppealResolutionSolutionDomain call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }


    private EditAppealResolutionSolutionDomain mappingResponse(Response<TkpdResponse> response) {
        EditAppealSolutionResponse editAppealSolutionResponse =
                response.body().convertDataObj(EditAppealSolutionResponse.class);
        EditAppealResolutionSolutionDomain model =
                new EditAppealResolutionSolutionDomain(response.isSuccessful(),
                        editAppealSolutionResponse != null ?
                                editAppealSolutionResponse.getSuccessMessage() :
                                null);
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
}
