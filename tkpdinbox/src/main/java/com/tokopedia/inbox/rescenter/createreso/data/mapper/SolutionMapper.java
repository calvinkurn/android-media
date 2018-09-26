package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem.AmountResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.FreeReturnResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.RequireResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.SolutionComplaintResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.SolutionOrderDetailResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.SolutionOrderResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.SolutionProblemAmountResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.SolutionProblemResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.SolutionProductImageResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.SolutionProductResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.SolutionResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.SolutionResponseResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.SolutionShippingResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.AmountDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.FreeReturnDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.RequireDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionComplaintDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionOrderDetailDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionOrderDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionProblemAmountDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionProblemDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionProductDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionProductImageDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionShippingDomain;
import com.tokopedia.inbox.rescenter.network.ResolutionResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 24/08/17.
 */

public class SolutionMapper implements Func1<Response<ResolutionResponse<SolutionResponseResponse>>, SolutionResponseDomain> {

    @Inject
    public SolutionMapper() {
    }

    @Override
    public SolutionResponseDomain call(Response<ResolutionResponse<SolutionResponseResponse>> dataResponseResponse) {
        return mappingResponse(dataResponseResponse);
    }

    private SolutionResponseDomain mappingResponse(Response<ResolutionResponse<SolutionResponseResponse>> response) {
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
                response.body().getData();
        return new SolutionResponseDomain(
                solutionResponseResponse.getSolution().size() != 0 ?
                        mappingSolutionDomain(solutionResponseResponse.getSolution()) :
                        new ArrayList<SolutionDomain>(),
                mappingRequireDomain(solutionResponseResponse.getRequire()),
                solutionResponseResponse.getFreeReturn() != null ?
                        mappingFreeReturnDomain(solutionResponseResponse.getFreeReturn())
                        : null,
                solutionResponseResponse.getComplaints() != null ?
                        mappingSolutionComplaintDomain(solutionResponseResponse.getComplaints()) :
                        null);
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

    public static List<SolutionComplaintDomain> mappingSolutionComplaintDomain(List<SolutionComplaintResponse> responseList) {
        List<SolutionComplaintDomain> domainList = new ArrayList<>();
        for (SolutionComplaintResponse response : responseList) {
            SolutionComplaintDomain domain = new SolutionComplaintDomain(
                    response.getProblem() != null ? mappingSolutionProblemDomain(response.getProblem()) : null,
                    response.getShipping() != null ? mappingSolutionShippingDomain(response.getShipping()) : null,
                    response.getProduct() != null ? mappingSolutionProductDomain(response.getProduct()) : null,
                    response.getOrder() != null ? mappingSolutionOrderDomain(response.getOrder()) : null
            );
            domainList.add(domain);
        }
        return domainList;
    }

    private static SolutionProblemDomain mappingSolutionProblemDomain(SolutionProblemResponse response) {
        return new SolutionProblemDomain(
                response.getType(),
                response.getName(),
                response.getTrouble(),
                response.getAmount() != null ?
                        mappingSolutionProblemAmountDomain(response.getAmount()) :
                        null,
                response.getMaxAmount() != null ?
                        mappingSolutionProblemAmountDomain(response.getMaxAmount()) :
                        null,
                response.getQty(),
                response.getRemark());
    }

    private static SolutionProblemAmountDomain mappingSolutionProblemAmountDomain(SolutionProblemAmountResponse response) {
        return new SolutionProblemAmountDomain(response.getIdr(), response.getInteger());
    }

    private static SolutionShippingDomain mappingSolutionShippingDomain(SolutionShippingResponse response) {
        return new SolutionShippingDomain(response.getFee(), response.isChecked());
    }

    private static SolutionProductDomain mappingSolutionProductDomain(SolutionProductResponse response) {
        return new SolutionProductDomain(
                response.getName(),
                response.getPrice(),
                response.getImage() != null ?
                        mappingSolutionProductImageDomain(response.getImage()) :
                        null);
    }

    private static SolutionProductImageDomain mappingSolutionProductImageDomain(SolutionProductImageResponse response) {
        return new SolutionProductImageDomain(response.getFull(), response.getThumb());
    }

    private static SolutionOrderDomain mappingSolutionOrderDomain(SolutionOrderResponse response) {
        return new SolutionOrderDomain(response.getDetail() != null ? mappingSolutionOrderDetailDomain(response.getDetail()) : null);
    }

    private static SolutionOrderDetailDomain mappingSolutionOrderDetailDomain(SolutionOrderDetailResponse response) {
        return new SolutionOrderDetailDomain(response.getId(), response.isFreeReturn());
    }

}
