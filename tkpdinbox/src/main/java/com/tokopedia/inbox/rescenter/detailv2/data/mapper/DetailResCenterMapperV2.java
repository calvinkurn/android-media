package com.tokopedia.inbox.rescenter.detailv2.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.ActionByResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.AddressResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.AmountResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.ByResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.ComplainedProductResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.CreateByResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.CustomerResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.DetailResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.FirstResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.InvoiceResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.LastResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.LogResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.OrderResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.PictureResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.ProductResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.ResolutionResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.RoleResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.SellerAddressResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.ShippingResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.ShopResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.SolutionResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.StatusResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.TroubleResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.UserAwbResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.ButtonResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.LastSolutionResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.NextActionDetailResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.NextActionDetailStepResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.NextActionResponse;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.ActionByData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.AddressData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.AmountData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.ByData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.ComplainedProductData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.CreateByData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.CustomerData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.DetailData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.FirstData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.InvoiceData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.LastData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.LogData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.OrderData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.PictureData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.ProductData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.ResolutionData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.RoleData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.SellerAddressData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.ShippingData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.ShopData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.SolutionData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.StatusData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.TroubleData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.UserAwbData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ButtonDomain;
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
 * Created by yfsx on 07/11/17.
 */

public class DetailResCenterMapperV2 implements Func1<Response<TkpdResponse>, DetailData> {
    private static final String DEFAULT_ERROR = "Terjadi kesalahan, mohon coba kembali.";
    private static final String ERROR_MESSAGE = "message_error";

    @Override
    public DetailData call(Response<TkpdResponse> response) {
        DetailResponse detailResponse = response.body().convertDataObj(
                DetailResponse.class);
        DetailData model = mappingResponse(detailResponse);
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

    public DetailData mappingResponse(DetailResponse response) {
        return new DetailData(
                response.getFirst() != null ?
                        mappingFirstData(response.getFirst()) :
                        null,
                response.getLast() != null ?
                        mappingLastData(response.getLast()) :
                        null,
                response.getButton() != null ?
                        mappingButtonDomain(response.getButton()) :
                        null,
                response.getShop() != null ?
                        mappingShopData(response.getShop()) :
                        null,
                response.getCustomer() != null ?
                        mappingCustomerData(response.getCustomer()) :
                        null,
                response.getOrder() != null ?
                        mappingOrderData(response.getOrder()) :
                        null,
                response.getResolution() != null ?
                        mappingResolutionData(response.getResolution()) :
                        null,
                response.getActionBy() != null ?
                        mappingActionByData(response.getActionBy()) :
                        null,
                response.getNextAction() != null ?
                        mappingNextActionDomain(response.getNextAction()) :
                        null,
                response.getLogs().size() != 0 ?
                        mappingLogDataList(response.getLogs()) :
                        null);
    }

    private FirstData mappingFirstData(FirstResponse response) {
        return new FirstData(response.getBuyerRemark());
    }

    private LastData mappingLastData(LastResponse response) {
        return new LastData(
                response.getSellerAddressResponse() != null ?
                        mappingSellerAddressData(response.getSellerAddressResponse()) :
                        null,
                response.getUserAwb() != null ?
                        mappingUserAwbData(response.getUserAwb()) :
                        null,
                response.getSolution() != null ?
                        mappingSolutionData(response.getSolution()) :
                        null,
                response.getProblem(),
                response.getStatus(),
                response.getComplainedProductResponses().size() != 0 ?
                        mappingComplainedProductList(response.getComplainedProductResponses()) :
                        null);
    }

    private SellerAddressData mappingSellerAddressData(SellerAddressResponse response) {
        return new SellerAddressData(
                response.getAddress() != null ?
                        mappingAddressData(response.getAddress()) :
                        null,
                response.getBy() != null ?
                        mappingByData(response.getBy()) :
                        null,
                response.getCreateTime(),
                response.getCreateTimeStr(),
                response.getConversationId());
    }

    private AddressData mappingAddressData(AddressResponse response) {
        return new AddressData(
                response.getAddressId(),
                response.getAddress(),
                response.getDistrict(),
                response.getCity(),
                response.getProvince(),
                response.getPhone(),
                response.getCountry(),
                response.getPostalCode(),
                response.getReceiver());
    }

    private UserAwbData mappingUserAwbData(UserAwbResponse response) {
        return new UserAwbData(
                response.getResConvId(),
                response.getAwb(),
                response.getShipping() != null ?
                        mappingShippingData(response.getShipping()) :
                        null,
                response.getBy() != null ?
                        mappingByData(response.getBy()) :
                        null,
                response.getTrackable(),
                response.getCreateTime(),
                response.getCreateTimeStr());
    }

    private ShippingData mappingShippingData(ShippingResponse response) {
        return new ShippingData(response.getId(), response.getName());
    }

    private ByData mappingByData(ByResponse response) {
        return new ByData(
                response.getId(),
                response.getName(),
                response.getPicture() != null ?
                        mappingPictureData(response.getPicture()) :
                        null,
                response.getRole() != null ?
                        mappingRoleData(response.getRole()) :
                        null);
    }

    private RoleData mappingRoleData(RoleResponse response) {
        return new RoleData(response.getId(), response.getName());
    }

    private List<ComplainedProductData> mappingComplainedProductList(List<ComplainedProductResponse> responseList) {
        List<ComplainedProductData> domainList = new ArrayList<>();
        for (ComplainedProductResponse response : responseList) {
            ComplainedProductData domain = new ComplainedProductData(
                    response.getId(),
                    response.getCount(),
                    response.getProduct() != null ?
                            mappingProductData(response.getProduct()) :
                            null,
                    response.getTrouble() != null ?
                            mappingTroubleData(response.getTrouble()) :
                            null);
            domainList.add(domain);
        }
        return domainList;
    }

    private ProductData mappingProductData(ProductResponse response) {
        return new ProductData(
                response.getName(),
                response.getThumb(),
                response.getVariant(),
                mappingAmountData(response.getAmount()),
                response.getQuantity());
    }

    private TroubleData mappingTroubleData(TroubleResponse response) {
        return new TroubleData(response.getId(), response.getName());
    }

    private ButtonDomain mappingButtonDomain(ButtonResponse response) {
        return new ButtonDomain(response.getReport(),
                response.getReportLabel(),
                response.getReportText(),
                response.getCancel(),
                response.getCancelLabel(),
                response.getCancelText(),
                response.getEdit(),
                response.getEditLabel(),
                response.getEditText(),
                response.getInputAddress(),
                response.getInputAddressLabel(),
                response.getInputAddressText(),
                response.getAppeal(),
                response.getAppealLabel(),
                response.getAppealText(),
                response.getInputAWB(),
                response.getInputAWBLabel(),
                response.getInputAWBText(),
                response.getAccept(),
                response.getAcceptLabel(),
                response.getAcceptText(),
                response.getAcceptReturn(),
                response.getAcceptReturnLabel(),
                response.getAcceptReturnText(),
                response.getFinish(),
                response.getFinishLabel(),
                response.getFinishText(),
                response.getAcceptByAdmin(),
                response.getAcceptByAdminLabel(),
                response.getAcceptByAdminText(),
                response.getAcceptByAdminReturn(),
                response.getAcceptByAdminReturnLabel(),
                response.getAcceptByAdminReturnText(),
                response.getRecomplaint(),
                response.getRecomplaintLabel(),
                response.getRecomplaintText());
    }

    private ShopData mappingShopData(ShopResponse response) {
        return new ShopData(
                response.getId(),
                response.getName(),
                response.getPicture() != null ?
                        mappingPictureData(response.getPicture()) :
                        null);
    }

    private CustomerData mappingCustomerData(CustomerResponse response) {
        return new CustomerData(
                response.getId(),
                response.getName(),
                response.getPicture() !=null ?
                        mappingPictureData(response.getPicture()) :
                        null);
    }

    private OrderData mappingOrderData(OrderResponse response) {
        return new OrderData(
                response.getId(),
                response.getInvoice() != null ?
                        mappingInvoiceData(response.getInvoice()) :
                        null,
                response.getOpenAmount(),
                response.getShippingPrice(),
                response.getAwb(),
                response.getPdf());
    }

    private InvoiceData mappingInvoiceData(InvoiceResponse response) {
        return new InvoiceData(response.getRefNum(), response.getUrl());
    }

    private ResolutionData mappingResolutionData(ResolutionResponse response) {
        return new ResolutionData(
                response.getId(),
                response.getStatus() != null ?
                        mappingStatusData(response.getStatus()) :
                        null,
                response.getCreateBy() != null?
                        mappingCreatedByData(response.getCreateBy()) :
                        null,
                response.getCreateTime(),
                response.getCreateTimeStr(),
                response.getExpireTime(),
                response.getExpireTimeStr(),
                response.getUpdateBy() != null ?
                        mappingCreatedByData(response.getUpdateBy()) :
                        null,
                response.getUpdateTime(),
                response.getFreeReturn());
    }

    private StatusData mappingStatusData(StatusResponse response) {
        return new StatusData(response.getId(), response.getName());
    }


    private NextActionDomain mappingNextActionDomain(NextActionResponse response) {
        return new NextActionDomain(response.getLast(),
                response.getDetail() != null ?
                        mappingNextActionDetailDomain(response.getDetail()) :
                        null);
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

    private LastDomain mappingLastDomain(com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.LastResponse response) {
        return new LastDomain(response.getSolution() != null ?
                mappingLastSolutionDomain(response.getSolution()) :
                null,
                response.getProblem());
    }

    private List<NextActionDetailStepDomain> mappingNextActionDetailStepDomainList(
            List<NextActionDetailStepResponse> responseList) {
        List<NextActionDetailStepDomain> domain = new ArrayList<>();
        for (NextActionDetailStepResponse response : responseList) {
            domain.add(new NextActionDetailStepDomain(response.getStatus(), response.getName()));
        }
        return domain;
    }

    private LastSolutionDomain mappingLastSolutionDomain(LastSolutionResponse response) {
        return new LastSolutionDomain(response.getId(), response.getName(), response.getAmount());
    }

    private List<LogData> mappingLogDataList(List<LogResponse> responseList) {
        List<LogData> domainList = new ArrayList<>();
        for (LogResponse response : responseList) {
            LogData domain = new LogData(response.getId(),
                    response.getAction(),
                    response.getSolution() != null ?
                            mappingSolutionData(response.getSolution()) :
                            null,
                    response.getActionBy() != null ?
                            mappingCreatedByData(response.getActionBy()) :
                            null,
                    response.getCreateBy() != null ?
                            mappingCreatedByData(response.getCreateBy()) :
                            null,
                    response.getCreateTime(),
                    response.getCreateTimeStr());
            domainList.add(domain);
        }
        return domainList;
    }

    private CreateByData mappingCreatedByData(CreateByResponse response) {
        return new CreateByData(response.getId(), response.getName(), mappingPictureData(response.getPicture()));
    }

    private PictureData mappingPictureData(PictureResponse response) {
        return new PictureData(response.getFullUrl(), response.getThumbnail());
    }

    private ActionByData mappingActionByData(ActionByResponse response) {
        return new ActionByData(response.getId(), response.getName());
    }

    private AmountData mappingAmountData(AmountResponse response) {
        return new AmountData(response.getIdr(), response.getInteger());
    }

    private SolutionData mappingSolutionData(SolutionResponse response) {
        return new SolutionData(
                response.getId(),
                response.getName(),
                response.getNameCustom(),
                response.getActionBy(),
                response.getAmount() != null ?
                        mappingAmountData(response.getAmount()) :
                        null,
                response.getCreateTime(),
                response.getCreateTimeStr());
    }
}
