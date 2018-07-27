package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem.AmountResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.EditFreeReturnResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.EditSolutionResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.EditSolutionResponseResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.AmountDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.EditSolutionDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.EditSolutionResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.FreeReturnDomain;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 24/08/17.
 */

public class EditSolutionMapper implements Func1<Response<DataResponse<EditSolutionResponseResponse>>, EditSolutionResponseDomain> {

    @Inject
    public EditSolutionMapper() {
    }

    @Override
    public EditSolutionResponseDomain call(Response<DataResponse<EditSolutionResponseResponse>> response) {
        return mappingResponse(response);
    }

    private EditSolutionResponseDomain mappingResponse(Response<DataResponse<EditSolutionResponseResponse>> response) {
        EditSolutionResponseResponse editSolutionResponseResponse =
                response.body().getData();
        return new EditSolutionResponseDomain(
                editSolutionResponseResponse.getSolution().size() != 0 ?
                        mappingSolutionDomain(editSolutionResponseResponse.getSolution()) :
                        new ArrayList<EditSolutionDomain>(),
                editSolutionResponseResponse.getFreeReturn() != null ?
                        mappingFreeReturnDomain(editSolutionResponseResponse.getFreeReturn()) :
                        null);
    }

    private List<EditSolutionDomain> mappingSolutionDomain(List<EditSolutionResponse> response) {
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

    private AmountDomain mappingAmountDomain(AmountResponse response) {
        return new AmountDomain(response.getIdr(),
                response.getInteger());
    }

    private FreeReturnDomain mappingFreeReturnDomain(EditFreeReturnResponse response) {
        return new FreeReturnDomain(response.getInfo(), response.getLink());
    }
}
