package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem.AmountResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.FreeReturnResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.RequireResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.SolutionResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.SolutionResponseResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.AmountDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.FreeReturnDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.RequireDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionResponseDomain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 24/08/17.
 */

public class SolutionMapper implements Func1<Response<TkpdResponse>, SolutionResponseDomain> {

    @Override
    public SolutionResponseDomain call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }


    private SolutionResponseDomain mappingResponse(Response<TkpdResponse> response) {
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

        SolutionResponseResponse solutionResponseResponse =
                response.body().convertDataObj(SolutionResponseResponse.class);
        return new SolutionResponseDomain(
                solutionResponseResponse.getSolution().size() != 0 ?
                        mappingSolutionDomain(solutionResponseResponse.getSolution()) :
                        new ArrayList<SolutionDomain>(),
                mappingRequireDomain(solutionResponseResponse.getRequire()),
                solutionResponseResponse.getFreeReturn() != null ?
                        mappingFreeReturnDomain(solutionResponseResponse.getFreeReturn()) : null);
    }

    private List<SolutionDomain> mappingSolutionDomain(List<SolutionResponse> response) {
        List<SolutionDomain> domainList = new ArrayList<>();
        for (SolutionResponse solutionResponse : response) {
            SolutionDomain solutionDomain = new SolutionDomain(solutionResponse.getId(),
                    solutionResponse.getName(),
                    solutionResponse.getSolutionName(),
                    solutionResponse.getAmount() != null ?
                            mappingAmountDomain(solutionResponse.getAmount()) : null);
            domainList.add(solutionDomain);
        }
        return domainList;
    }

    private RequireDomain mappingRequireDomain(RequireResponse response) {
        return new RequireDomain(response.isAttachment());
    }

    private AmountDomain mappingAmountDomain(AmountResponse response) {
        return new AmountDomain(response.getIdr(),
                response.getInteger());
    }

    private FreeReturnDomain mappingFreeReturnDomain(FreeReturnResponse response) {
        return new FreeReturnDomain(response.getInfo(), response.getLink());
    }

}
