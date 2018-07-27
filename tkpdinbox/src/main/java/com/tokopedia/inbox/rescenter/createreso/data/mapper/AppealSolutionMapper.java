package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem.AmountResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.AppealFreeReturnResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.AppealSolutionResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.AppealSolutionResponseResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.AmountDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.AppealSolutionDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.AppealSolutionResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.FreeReturnDomain;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 24/08/17.
 */

public class AppealSolutionMapper implements Func1<Response<DataResponse<AppealSolutionResponseResponse>>, AppealSolutionResponseDomain> {

    @Inject
    public AppealSolutionMapper() {
    }

    @Override
    public AppealSolutionResponseDomain call(Response<DataResponse<AppealSolutionResponseResponse>> response) {
        return mappingResponse(response);
    }

    private AppealSolutionResponseDomain mappingResponse(Response<DataResponse<AppealSolutionResponseResponse>> response) {

        AppealSolutionResponseResponse appealSolutionResponseResponse =
                response.body().getData();
        return new AppealSolutionResponseDomain(
                appealSolutionResponseResponse.getSolution().size() != 0 ?
                        mappingSolutionDomain(appealSolutionResponseResponse.getSolution()) :
                        new ArrayList<AppealSolutionDomain>(),
                appealSolutionResponseResponse.getFreeReturn() != null ?
                        mappingFreeReturnDomain(appealSolutionResponseResponse.getFreeReturn()) :
                        null);
    }

    private List<AppealSolutionDomain> mappingSolutionDomain(List<AppealSolutionResponse> response) {
        List<AppealSolutionDomain> domainList = new ArrayList<>();
        for (AppealSolutionResponse solutionResponse : response) {
            AppealSolutionDomain solutionDomain = new AppealSolutionDomain(solutionResponse.getId(),
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

    private FreeReturnDomain mappingFreeReturnDomain(AppealFreeReturnResponse response) {
        return new FreeReturnDomain(response.getInfo(), response.getLink());
    }
}
