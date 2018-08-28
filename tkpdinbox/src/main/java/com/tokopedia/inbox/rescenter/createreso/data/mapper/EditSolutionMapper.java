package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem.AmountResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.CurrentSolutionResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.EditAppealSolutionResponseResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.EditFreeReturnResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.EditSolutionResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.SolutionMessageResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.SolutionProblemAmountResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.AmountDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.CurrentSolutionDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.EditSolutionDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.EditSolutionResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.FreeReturnDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionMessageDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionProblemAmountDomain;
import com.tokopedia.inbox.rescenter.network.ResolutionResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 24/08/17.
 */

public class EditSolutionMapper implements Func1<Response<ResolutionResponse<EditAppealSolutionResponseResponse>>, EditSolutionResponseDomain> {

    @Inject
    public EditSolutionMapper() {
    }

    @Override
    public EditSolutionResponseDomain call(Response<ResolutionResponse<EditAppealSolutionResponseResponse>> response) {
        return mappingResponse(response);
    }

    public static EditSolutionResponseDomain mappingResponse(Response<ResolutionResponse<EditAppealSolutionResponseResponse>> response) {
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
        EditAppealSolutionResponseResponse editAppealSolutionResponseResponse =
                response.body().getData();
        return new EditSolutionResponseDomain(
                editAppealSolutionResponseResponse.getCurrentSolution() != null ?
                        mappingCurrentSolutionDomain(editAppealSolutionResponseResponse.getCurrentSolution()) :
                        null,
                editAppealSolutionResponseResponse.getSolution().size() != 0 ?
                        mappingSolutionDomain(editAppealSolutionResponseResponse.getSolution()) :
                        new ArrayList<EditSolutionDomain>(),
                editAppealSolutionResponseResponse.getFreeReturn() != null ?
                        mappingFreeReturnDomain(editAppealSolutionResponseResponse.getFreeReturn()) :
                        null,
                editAppealSolutionResponseResponse.getComplaints() != null ?
                        SolutionMapper.mappingSolutionComplaintDomain(editAppealSolutionResponseResponse.getComplaints())
                        : null,
                editAppealSolutionResponseResponse.getMessage() != null ?
                        mappingSolutionMessageDomain(editAppealSolutionResponseResponse.getMessage()) :
                        null);
    }

    private static SolutionMessageDomain mappingSolutionMessageDomain(SolutionMessageResponse response) {
        return new SolutionMessageDomain(response.getConfirm());
    }

    private static CurrentSolutionDomain mappingCurrentSolutionDomain(CurrentSolutionResponse response) {
        return new CurrentSolutionDomain(
                response.getId(),
                response.getName(),
                response.getMessage(),
                response.getIdentifier(),
                response.getAmount() != null ?
                        mappingSolutionProblemAmountDomain(response.getAmount()) :
                        null);
    }

    private static List<EditSolutionDomain> mappingSolutionDomain(List<EditSolutionResponse> response) {
        List<EditSolutionDomain> domainList = new ArrayList<>();
        for (EditSolutionResponse solutionResponse : response) {
            EditSolutionDomain solutionDomain = new EditSolutionDomain(solutionResponse.getId(),
                    solutionResponse.getName(),
                    solutionResponse.getSolutionName(),
                    solutionResponse.getAmount() != null ?
                            mappingAmountDomain(solutionResponse.getAmount()) :
                            null);
            domainList.add(solutionDomain);
        }
        return domainList;
    }

    private static SolutionProblemAmountDomain mappingSolutionProblemAmountDomain(SolutionProblemAmountResponse response) {
        return new SolutionProblemAmountDomain(response.getIdr(), response.getInteger());
    }

    private static AmountDomain mappingAmountDomain(AmountResponse response) {
        return new AmountDomain(response.getIdr(),
                response.getInteger());
    }

    private static FreeReturnDomain mappingFreeReturnDomain(EditFreeReturnResponse response) {
        return new FreeReturnDomain(response.getInfo(), response.getLink());
    }
}
