package com.tokopedia.inbox.rescenter.detailv2.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.DetailResCenterCustomer;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.DetailResCenterEntity;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.DetailResCenterHistory;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.DetailResCenterLast;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.DetailResCenterLastComplainedProduct;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.DetailResCenterLastShipping;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.DetailResCenterLastSolution;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.DetailResCenterOrder;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.DetailResCenterResolution;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.DetailResCenterShop;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.AddressDomainModel;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.AwbAttachmentDomainModel;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ButtonDomainModel;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.DetailResCenter;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ProductComplainedDomainModel;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ProductDataDomainModel;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ResolutionDomainModel;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ResolutionHistoryDomainModel;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ResolutionHistoryItemDomainModel;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ShippingDomainModel;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.SolutionDomainModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 3/9/17.
 */

public class DetailResCenterMapper implements Func1<Response<TkpdResponse>, DetailResCenter> {

    public DetailResCenterMapper() {
    }

    @Override
    public DetailResCenter call(Response<TkpdResponse> response) {
        DetailResCenter domainModel = new DetailResCenter();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                DetailResCenterEntity entity
                        = response.body().convertDataObj(DetailResCenterEntity.class);
                domainModel.setSuccess(true);
                domainModel.setAddress(mappingAddress(entity));
                domainModel.setButton(mappingButton(entity));
                domainModel.setProductData(mappingProductData(entity.getLast().getComplainedProduct()));
                domainModel.setResolution(mappingResolution(entity));
                domainModel.setResolutionHistory(mappingHistoryData(entity.getHistory()));
                domainModel.setShipping(mappingAwbReturData(entity.getLast().getShipping()));
                domainModel.setSolutionData(mappingSolutionData(entity.getLast().getSolution()));
            } else {
                domainModel.setSuccess(false);
                domainModel.setMessageError(generateMessageError(response));
            }
        } else {
            domainModel.setSuccess(false);
            domainModel.setErrorCode(response.code());
        }
        return domainModel;
    }

    private String generateMessageError(Response<TkpdResponse> response) {
        return response.body().getErrorMessageJoined();
    }

    private AddressDomainModel mappingAddress(DetailResCenterEntity entity) {
        if (entity.getLast().getAddress() == null) {
            return null;
        }
        AddressDomainModel data = new AddressDomainModel();
        data.setCity(entity.getLast().getAddress().getCity().getName());
        data.setDate("");
        data.setDistrict(entity.getLast().getAddress().getDistrict().getName());
        data.setPhoneReceiver(entity.getLast().getAddress().getReceiver().getPhone());
        data.setPostalCode(entity.getLast().getAddress().getPostalCode());
        data.setProvince(entity.getLast().getAddress().getProvince().getName());
        data.setReceiver(entity.getLast().getAddress().getReceiver().getName());
        data.setStreet(entity.getLast().getAddress().getStreet());
        return data;
    }

    private ShippingDomainModel mappingAwbReturData(List<DetailResCenterLastShipping> entity) {
        if (entity == null || entity.isEmpty()) {
            return null;
        }

        ShippingDomainModel data = new ShippingDomainModel();
        for (int i = 0; i < entity.size(); i++) {
            if (i == 0) {
                DetailResCenterLastShipping shipping = entity.get(i);
                data.setShipmentRef(shipping.getShippingRefNum());
                data.setShipmentDate(shipping.getCreateTimeStr());
                data.setShipmentID(shipping.getShippingId());
//                data.setProviderText(shipping.getActionByText());
                data.setProviderText("Not yet set");
                data.setAttachment(mappingShippingAttacment(shipping.getAttachments()));
            }
        }
        return data;
    }

    private List<AwbAttachmentDomainModel> mappingShippingAttacment(List<DetailResCenterLastShipping.Attachments> attachmentList) {
        if (attachmentList == null || attachmentList.isEmpty()) {
            return null;
        }

        List<AwbAttachmentDomainModel> viewModels = new ArrayList<>();
        for (DetailResCenterLastShipping.Attachments items : attachmentList) {
            AwbAttachmentDomainModel viewModel = new AwbAttachmentDomainModel();
            viewModel.setImageUrl(items.getImageThumb());
            viewModel.setImageThumbUrl(items.getUrl());
            viewModels.add(viewModel);
        }
        return viewModels;
    }

    private ButtonDomainModel mappingButton(DetailResCenterEntity entity) {
        if (entity.getButton() == null) {
            return null;
        }
        ButtonDomainModel data = new ButtonDomainModel();
        data.setAccept(entity.getButton().getAccept());
        data.setReport(entity.getButton().getReport());
        data.setReportDialogText(entity.getButton().getReportText());
        data.setCancel(entity.getButton().getCancel());
        data.setCancelDialogText(entity.getButton().getCancelText());
        data.setEdit(entity.getButton().getEdit());
        data.setInputAddress(entity.getButton().getInputAddress());
        data.setAppeal(entity.getButton().getAppeal());
        data.setInputAwb(entity.getButton().getInputResi());
        data.setFinish(entity.getButton().getFinish());
        data.setAcceptByAdmin(entity.getButton().getAcceptByAdmin());
        data.setAcceptProductDialogText(entity.getButton().getFinishAcceptText());
        return data;
    }

    private ResolutionDomainModel mappingResolution(DetailResCenterEntity entity) {
        DetailResCenterOrder order = entity.getOrder();
        DetailResCenterResolution resolution = entity.getResolution();
        DetailResCenterCustomer customer = entity.getCustomer();
        DetailResCenterShop shop = entity.getShop();
        DetailResCenterLast last = entity.getLast();
        DetailResCenterLastSolution lastSolution = last.getSolution();

        ResolutionDomainModel data = new ResolutionDomainModel();
        data.setOrderID(order.getId());
        data.setOrderAwbNumber("NOT SET OR ASK BACKEND");
        data.setBuyerID(customer.getId());
        data.setBuyerName(customer.getName());
        data.setComplaintDate(resolution.getCreateTimeStr());
        data.setInvoice(order.getInvoice().getRefNum());
        data.setInvoiceUrl(order.getInvoice().getUrl());
        // -----------
        data.setResponseDeadline("NOT SET OR ASK BACKEND");
        data.setSellerDeadlineVisibility(true);
        data.setBuyerDeadlineVisibility(true);
        // -----------
        data.setShopID(shop.getId());
        data.setShopName(shop.getName());
        data.setReceivedFlag(lastSolution.getReceivedFlag());
        data.setStatus(last.getStatus());
        return data;
    }

    private ResolutionHistoryDomainModel mappingHistoryData(List<DetailResCenterHistory> entity) {
        if (entity == null || entity.isEmpty()) {
            return  null;
        }

        ResolutionHistoryDomainModel data = new ResolutionHistoryDomainModel();
        List<ResolutionHistoryItemDomainModel> domainModels = new ArrayList<>();
        for (int i = 0; i < entity.size() ; i++) {
            DetailResCenterHistory item = entity.get(i);
            ResolutionHistoryItemDomainModel domainModel = new ResolutionHistoryItemDomainModel();
            domainModel.setRemark(item.getRemark());
            domainModel.setDate(item.getCreateTimeStr());
//            domainModel.setActionBy(item.getActionByText());
            domainModel.setActionBy("not yet set");
            domainModels.add(domainModel);
        }
        data.setList(domainModels);
        return data;
    }

    private ProductDataDomainModel mappingProductData(List<DetailResCenterLastComplainedProduct> entity) {
        if (entity == null || entity.isEmpty()) {
            return null;
        }

        ProductDataDomainModel data = new ProductDataDomainModel();
        List<ProductComplainedDomainModel> viewModels = new ArrayList<>();
        for (DetailResCenterLastComplainedProduct item : entity) {
            ProductComplainedDomainModel viewModel = new ProductComplainedDomainModel();
            viewModel.setProductName(item.getName());
            viewModel.setProductImageUrl(item.getPhoto().getImageThumb());
            viewModel.setProductID(item.getResProductId());
            viewModels.add(viewModel);
        }
        data.setList(viewModels);
        return data;
    }

    private SolutionDomainModel mappingSolutionData(DetailResCenterLastSolution entity) {
        SolutionDomainModel data = new SolutionDomainModel();
        data.setSolutionDate(entity.getCreateTimeStr());
//        data.setSolutionActionBy(entity.getActionByText());
        data.setSolutionActionBy("Not yet set");
        data.setSolutionRemark(entity.getName());
        return data;
    }

}
