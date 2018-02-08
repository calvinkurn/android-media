package com.tokopedia.inbox.rescenter.detailv2.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.ButtonResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.ConversationActionResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.ConversationAddressResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.ConversationAttachmentResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.ConversationButtonResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.ConversationCreateTimeResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.ConversationFlagResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.ConversationListResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.ConversationProductResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.ConversationResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.ConversationShippingDetailResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.ConversationSolutionResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.ConversationTroubleResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.CustomerResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.DetailResChatResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.LastResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.LastSolutionResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.NextActionDetailResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.NextActionDetailStepResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.NextActionResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.OrderResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.ResolutionResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.ShopResponse;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ButtonDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationActionDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationAddressDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationAttachmentDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationButtonDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationCreateTimeDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationFlagDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationListDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationProductDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationShippingDetailDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationSolutionDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationTroubleDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.CustomerDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.LastDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.LastSolutionDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDetailDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDetailStepDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.OrderDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ResolutionDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ShopDomain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

import static com.tokopedia.core.network.ErrorMessageException.DEFAULT_ERROR;

/**
 * Created by yoasfs on 10/10/17.
 */

public class GetDetailResChatMapper implements Func1<Response<TkpdResponse>, DetailResChatDomain> {

    @Override
    public DetailResChatDomain call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private DetailResChatDomain mappingResponse(Response<TkpdResponse> response) {
        DetailResChatResponse detailResChatResponse = response.body().convertDataObj(
                DetailResChatResponse.class);
        DetailResChatDomain model = new DetailResChatDomain(
                detailResChatResponse.getNextAction() != null ?
                        mappingNextActionDomain(detailResChatResponse.getNextAction()) :
                        null,
                detailResChatResponse.getShopResponse() != null ?
                        mappingShopDomain(detailResChatResponse.getShopResponse()) :
                        null,
                detailResChatResponse.getCustomer() != null ?
                        mappingCustomerDomain(detailResChatResponse.getCustomer()) :
                        null,
                detailResChatResponse.getResolution() != null ?
                        mappingResolutionDomain(detailResChatResponse.getResolution()) :
                        null,
                detailResChatResponse.getButton() != null ?
                        mappingButtonDomain(detailResChatResponse.getButton()) :
                        null,
                detailResChatResponse.getActionBy(),
                detailResChatResponse.getConversationList() != null ?
                        mappingConversationListDomain(detailResChatResponse.getConversationList()) :
                        null,
                detailResChatResponse.getOrderResponse() != null ?
                        mappingOrderDomain(detailResChatResponse.getOrderResponse()) :
                        null,
                detailResChatResponse.getLast() != null ?
                        mappingLastDomain(detailResChatResponse.getLast()) :
                        null);
        if (response.isSuccessful()) {
            if (response.raw().code() == ResponseStatus.SC_OK) {
                if (response.body().isNullData()) {
                    if (response.body().getErrorMessageJoined() != null || !response.body().getErrorMessageJoined().isEmpty()) {
                        throw new ErrorMessageException(response.body().getErrorMessageJoined());
                    } else {
                        throw new ErrorMessageException(DEFAULT_ERROR);
                    }
                } else {
                    model.setSuccess(true);
                }
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return model;
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

    private ShopDomain mappingShopDomain(ShopResponse response) {
        return new ShopDomain(response.getId(),
                response.getName(),
                response.getUrl(),
                response.getImageThumb(),
                response.getImage());
    }

    private CustomerDomain mappingCustomerDomain(CustomerResponse response) {
        return new CustomerDomain(response.getId(),
                response.getName(),
                response.getUrl(),
                response.getImageThumb(),
                response.getImage());
    }

    private ResolutionDomain mappingResolutionDomain(ResolutionResponse response) {
        return new ResolutionDomain(response.getId(), response.getFreeReturn(), response.getStatus());
    }

    private ButtonDomain mappingButtonDomain(ButtonResponse response) {
        return new ButtonDomain(response.getReport(),
                response.getReportLabel(),
                response.getReportText(),
                response.getReportOrder(),
                response.getCancel(),
                response.getCancelLabel(),
                response.getCancelText(),
                response.getCancelOrder(),
                response.getEdit(),
                response.getEditLabel(),
                response.getEditText(),
                response.getEditOrder(),
                response.getInputAddress(),
                response.getInputAddressLabel(),
                response.getInputAddressText(),
                response.getInputAddressOrder(),
                response.getAppeal(),
                response.getAppealLabel(),
                response.getAppealText(),
                response.getAppealOrder(),
                response.getInputAWB(),
                response.getInputAWBLabel(),
                response.getInputAWBText(),
                response.getInputAWBOrder(),
                response.getAccept(),
                response.getAcceptLabel(),
                response.getAcceptText(),
                response.getAcceptTextLite(),
                response.getAcceptOrder(),
                response.getAcceptReturn(),
                response.getAcceptReturnLabel(),
                response.getAcceptReturnText(),
                response.getAcceptReturnOrder(),
                response.getFinish(),
                response.getFinishLabel(),
                response.getFinishText(),
                response.getFinishOrder(),
                response.getAcceptByAdmin(),
                response.getAcceptByAdminLabel(),
                response.getAcceptByAdminText(),
                response.getAcceptByAdminOrder(),
                response.getAcceptByAdminReturn(),
                response.getAcceptByAdminReturnLabel(),
                response.getAcceptByAdminReturnText(),
                response.getAcceptByAdminReturnOrder(),
                response.getRecomplaint(),
                response.getRecomplaintLabel(),
                response.getRecomplaintText(),
                response.getRecomplaintOrder());
    }

    private ConversationListDomain mappingConversationListDomain(ConversationListResponse response) {
        return new ConversationListDomain(
                response.getCanLoadMore(),
                response.getConversation() != null ?
                        mappingConversationDomain(response.getConversation()) :
                        null);
    }

    private List<ConversationDomain> mappingConversationDomain(List<ConversationResponse> responseList) {
        List<ConversationDomain> domainList = new ArrayList<>();
        for (ConversationResponse response : responseList) {
            domainList.add(new ConversationDomain(response.getResConvId(),
                    response.getAction() != null ?
                            mappingConversationActionDomain(response.getAction()) :
                            null,
                    response.getMessage(),
                    response.getAddress() != null ?
                            mappingConversationAddressDomain(response.getAddress()) :
                            null,
                    response.getShippingDetail() != null ?
                            mappingConversationShippingDetailDomain(response.getShippingDetail()) :
                            null,
                    response.getCreateTime() != null ?
                            mappingConversationCreateTimeDomain(response.getCreateTime()) :
                            null,
                    mappingConversationAttachmentDomain(response.getAttachment()),
                    response.getTrouble() != null ?
                            mappingConversationTroubleDomain(response.getTrouble()) :
                            null,
                    response.getSolution() != null ?
                            mappingConversationSolutionDomain(response.getSolution()) :
                            null,
                    mappingConversationProductDomain(response.getProduct()),
                    response.getButton() != null ?
                            mappingConversationButtonDomain(response.getButton()) :
                            null,
                    response.getFlag() != null ?
                            mappingConversationFlagDomain(response.getFlag()) :
                            null));
        }
        return domainList;
    }

    private ConversationActionDomain mappingConversationActionDomain(ConversationActionResponse response) {
        return new ConversationActionDomain(response.getType(), response.getBy(), response.getTitle());
    }

    private ConversationCreateTimeDomain mappingConversationCreateTimeDomain(
            ConversationCreateTimeResponse response) {
        return new ConversationCreateTimeDomain(response.getTimestamp(), response.getString());
    }

    private List<ConversationAttachmentDomain> mappingConversationAttachmentDomain(
            List<ConversationAttachmentResponse> responseList) {
        List<ConversationAttachmentDomain> domainList = new ArrayList<>();
        if (responseList != null)
            for (ConversationAttachmentResponse response : responseList) {
                domainList.add(new ConversationAttachmentDomain(response.getType(),
                        response.getThumb(),
                        response.getFull()));
            }
        return domainList;
    }

    private ConversationAddressDomain mappingConversationAddressDomain(ConversationAddressResponse response) {
        return new ConversationAddressDomain(response.getAddressId(),
                response.getAddress(),
                response.getDistrict(),
                response.getCity(),
                response.getProvince(),
                response.getPhone(),
                response.getCountry(),
                response.getPostalCode(),
                response.getReceiver());
    }

    private ConversationShippingDetailDomain mappingConversationShippingDetailDomain(
            ConversationShippingDetailResponse response) {
        return new ConversationShippingDetailDomain(response.getAwbNumber(),
                response.getId(),
                response.getName());
    }

    private ConversationTroubleDomain mappingConversationTroubleDomain(
            ConversationTroubleResponse response) {
        return new ConversationTroubleDomain(response.getString());
    }

    private ConversationSolutionDomain mappingConversationSolutionDomain(
            ConversationSolutionResponse response) {
        return new ConversationSolutionDomain(response.getId(),
                response.getName(),
                response.getAmount(),
                response.getString());
    }

    private List<ConversationProductDomain> mappingConversationProductDomain(
            List<ConversationProductResponse> responseList) {
        List<ConversationProductDomain> domainList = new ArrayList<>();
        if (responseList != null)
            for (ConversationProductResponse response : responseList) {
            domainList.add(new ConversationProductDomain(
                    mappingConversationAttachmentDomainList(response.getImage()),
                    response.getMessage(),
                    response.getResId(),
                    response.getName()));
        }
        return domainList;
    }

    private List<ConversationAttachmentDomain> mappingConversationAttachmentDomainList(List<ConversationAttachmentResponse> responseList) {
        List<ConversationAttachmentDomain> domainList = new ArrayList<>();
        if (responseList != null)
            for (ConversationAttachmentResponse response : responseList) {
            domainList.add(new ConversationAttachmentDomain(response.getType(), response.getThumb(), response.getFull()));
        }
        return domainList;
    }

    private ConversationButtonDomain mappingConversationButtonDomain(ConversationButtonResponse response) {
        return new ConversationButtonDomain(response.getTrackAwb(),
                response.getEditAwb(),
                response.getEditAddress());
    }

    private ConversationFlagDomain mappingConversationFlagDomain(ConversationFlagResponse response) {
        return new ConversationFlagDomain(response.getSystem(), response.getSolution());
    }

    private OrderDomain mappingOrderDomain(OrderResponse response) {
        return new OrderDomain(response.getId(), response.getOpenAmount(), response.getShippingPrices());
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
