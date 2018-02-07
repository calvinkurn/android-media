package com.tokopedia.inbox.rescenter.detailv2.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.LastResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.LastSolutionResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.NextActionDetailResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.NextActionDetailStepResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.NextActionResponse;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.LastDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.LastSolutionDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDetailDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDetailStepDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 10/10/17.
 */

public class GetNextActionMapper implements Func1<Response<TkpdResponse>, NextActionDomain> {

    @Override
    public NextActionDomain call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private NextActionDomain mappingResponse(Response<TkpdResponse> response) {
        NextActionResponse detailResChatResponse = response.body().convertDataObj(
                NextActionResponse.class);
        if (response.isSuccessful()) {
            if (response.raw().code() == ResponseStatus.SC_OK) {
                if (response.body().isNullData()) {
                    if (response.body().getErrorMessageJoined() != null || !response.body().getErrorMessageJoined().isEmpty()) {
                        throw new ErrorMessageException(response.body().getErrorMessageJoined());
                    } else {
                        throw new ErrorMessageException(ErrorMessageException.DEFAULT_ERROR);
                    }
                } else {
                }
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return mappingNextActionDomain(detailResChatResponse);
    }

    private NextActionDomain mappingNextActionDomain(NextActionResponse response) {
        return new NextActionDomain(response.getLast(),
                response.getDetail() != null ?
                        mappingNextActionDetailDomain(response.getDetail()) :
                        null,
                response.getProblem());
    }

    private NextActionDetailDomain mappingNextActionDetailDomain(NextActionDetailResponse response) {
        return new NextActionDetailDomain(response.getSolution(),
                response.getLast() != null ?
                        mappingLastDomain(response.getLast()) :
                        null,
                response.getStep() != null ?
                        mappingNextActionDetailStepDomainList(response.getStep()) :
                        null);
    }

    private List<NextActionDetailStepDomain> mappingNextActionDetailStepDomainList(
            List<NextActionDetailStepResponse> responseList) {
        List<NextActionDetailStepDomain> domain = new ArrayList<>();
        for (NextActionDetailStepResponse response : responseList) {
            domain.add(new NextActionDetailStepDomain(response.getStatus(), response.getName()));
        }
        return domain;
    }

    private LastDomain mappingLastDomain(LastResponse response) {
        return new LastDomain(response.getSolution() != null ?
                mappingLastSolutionDomain(response.getSolution()) :
                null,
                response.getProblem());
    }

    private LastSolutionDomain mappingLastSolutionDomain(LastSolutionResponse response) {
        return new LastSolutionDomain(response.getId(), response.getName(), response.getAmount());
    }
}
