package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem.AmountResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem.OrderDetailResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem.OrderProductResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem.OrderResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem.ProblemResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem.ProductProblemListResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem.ProductProblemResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem.ShippingDetailResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem.ShippingResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem.StatusInfoResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem.StatusResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem.StatusTroubleResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.AmountDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.OrderDetailDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.OrderDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.OrderProductDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ProblemDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ProductProblemDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ProductProblemResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ShippingDetailDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ShippingDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.StatusDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.StatusInfoDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.StatusTroubleDomain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

import static com.tokopedia.core.network.ErrorMessageException.DEFAULT_ERROR;

/**
 * Created by yoasfs on 16/08/17.
 */

public class GetProductProblemMapper implements Func1<Response<TkpdResponse>, ProductProblemResponseDomain> {

    @Override
    public ProductProblemResponseDomain call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private ProductProblemResponseDomain mappingResponse(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if (response.raw().code() == ResponseStatus.SC_OK) {
                if (response.body().isNullData()) {
                    if (response.body().getErrorMessageJoined() != null || !response.body().getErrorMessageJoined().isEmpty()) {
                        throw new ErrorMessageException(response.body().getErrorMessageJoined());
                    } else {
                        throw new ErrorMessageException(DEFAULT_ERROR);
                    }
                }
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        ProductProblemListResponse productProblemListResponse = response.body().convertDataObj(
                ProductProblemListResponse.class);
        return new ProductProblemResponseDomain(
                mappingProductProblemListDomain(
                        productProblemListResponse.getProductProblemResponseList()));
    }

    private List<ProductProblemDomain> mappingProductProblemListDomain(
            List<ProductProblemResponse> response) {
        List<ProductProblemDomain> domainList = new ArrayList<>();
        for (ProductProblemResponse productProblemResponse : response) {
            ProductProblemDomain domain = new ProductProblemDomain(
                    productProblemResponse.getProblem() != null ?
                            mappingProblemDomain(productProblemResponse.getProblem()) : null,
                    productProblemResponse.getOrder() != null ?
                            mappingOrderDomain(productProblemResponse.getOrder()) : null,
                    productProblemResponse.getStatusList() != null ?
                            mappingStatusDomain(productProblemResponse.getStatusList()) : null);
            domainList.add(domain);
        }
        return domainList;
    }

    private ProblemDomain mappingProblemDomain(ProblemResponse problemResponse) {
        ProblemDomain domain = new ProblemDomain(problemResponse.getType(),
                problemResponse.getName());
        return domain;
    }

    private OrderDomain mappingOrderDomain(OrderResponse orderResponse) {
        OrderDomain orderDomain = new OrderDomain(
                orderResponse.getDetail() != null ?
                        mappingOrderDetailDomain(orderResponse.getDetail()) :
                        null,
                orderResponse.getProduct() != null ?
                        mappingOrderProductDomain(orderResponse.getProduct()) :
                        null,
                orderResponse.getShipping() != null ?
                        mappingShippingDomain(orderResponse.getShipping()) :
                        null);
        return orderDomain;
    }

    private OrderDetailDomain mappingOrderDetailDomain(OrderDetailResponse orderDetailResponse) {
        OrderDetailDomain domain = new OrderDetailDomain(orderDetailResponse.getId(),
                orderDetailResponse.getReturnable());
        return domain;
    }

    private OrderProductDomain mappingOrderProductDomain(OrderProductResponse orderProductResponse) {
        OrderProductDomain domain = new OrderProductDomain(orderProductResponse.getName(),
                orderProductResponse.getThumb(),
                orderProductResponse.getVariant(),
                orderProductResponse.getQuantity(),
                orderProductResponse.getAmount() != null ?
                        mappingAmountDomain(orderProductResponse.getAmount()) :
                        null);
        return domain;
    }

    private AmountDomain mappingAmountDomain(AmountResponse amountResponse) {
        AmountDomain domain = new AmountDomain(amountResponse.getIdr(),
                amountResponse.getInteger());
        return domain;
    }

    private ShippingDomain mappingShippingDomain(ShippingResponse shippingResponse) {
        ShippingDomain domain = new ShippingDomain(shippingResponse.getId(),
                shippingResponse.getName(),
                shippingResponse.getDetail() != null ?
                        mappingShippingDetailDomain(shippingResponse.getDetail()) :
                        null);

        return domain;
    }

    private ShippingDetailDomain mappingShippingDetailDomain(
            ShippingDetailResponse shippingDetailResponse) {
        ShippingDetailDomain domain = new ShippingDetailDomain(
                shippingDetailResponse.getId(),
                shippingDetailResponse.getName());
        return domain;
    }


    private List<StatusDomain> mappingStatusDomain(List<StatusResponse> statusResponseList) {
        List<StatusDomain> statusDomainList = new ArrayList<>();

        for (StatusResponse statusResponse : statusResponseList) {
            StatusDomain domain = new StatusDomain(
                    statusResponse.isDelivered(),
                    statusResponse.getName(),
                    statusResponse.getTrouble() != null ?
                            mappingStatusTrouble(statusResponse.getTrouble()) :
                            null,
                    statusResponse.getInfo() != null ?
                            mappingStatusInfoDomain(statusResponse.getInfo()) :
                            null);
            statusDomainList.add(domain);
        }
        return statusDomainList;
    }

    private List<StatusTroubleDomain> mappingStatusTrouble(
            List<StatusTroubleResponse> statusTroubleResponseList) {
        List<StatusTroubleDomain> domainList = new ArrayList<>();
        for (StatusTroubleResponse statusTroubleResponse : statusTroubleResponseList) {
            StatusTroubleDomain domain = new StatusTroubleDomain(
                    statusTroubleResponse.getId(),
                    statusTroubleResponse.getName());
            domainList.add(domain);
        }
        return domainList;
    }

    private StatusInfoDomain mappingStatusInfoDomain(StatusInfoResponse statusInfoResponse) {
        StatusInfoDomain domain = new StatusInfoDomain(
                statusInfoResponse.isShow(),
                statusInfoResponse.getDate());
        return domain;
    }
}
