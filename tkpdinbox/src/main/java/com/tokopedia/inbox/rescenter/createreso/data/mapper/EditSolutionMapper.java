package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
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

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 24/08/17.
 */

public class EditSolutionMapper implements Func1<Response<TkpdResponse>, EditSolutionResponseDomain> {

    @Override
    public EditSolutionResponseDomain call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }


    private EditSolutionResponseDomain mappingResponse(Response<TkpdResponse> response) {
        EditSolutionResponseResponse editSolutionResponseResponse =
                response.body().convertDataObj(EditSolutionResponseResponse.class);
        EditSolutionResponseDomain model = new EditSolutionResponseDomain(
                editSolutionResponseResponse.getSolution().size() != 0 ?
                        mappingSolutionDomain(editSolutionResponseResponse.getSolution()) :
                        new ArrayList<EditSolutionDomain>(),
                editSolutionResponseResponse.getFreeReturn() != null ?
                        mappingFreeReturnDomain(editSolutionResponseResponse.getFreeReturn()) :
                        null);
        if (response.isSuccessful()) {
            if (response.body().isNullData()) {
                if (response.body().getErrorMessageJoined() != null || !response.body().getErrorMessageJoined().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException("");
                }
            } else {
                model.setSuccess(true);
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return model;
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
