package com.tokopedia.inbox.rescenter.detailv2.view.subscriber;

import android.util.Log;

import com.tkpd.library.utils.Logger;
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
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.AddressReturData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.AwbAttachmentViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.AwbData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.ButtonData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.DetailData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.DetailViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.HistoryData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.HistoryItem;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.ProductData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.ProductItem;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.SolutionData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.StatusData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 3/16/17.
 */

public class GetResCenterDetailSubscriber extends rx.Subscriber<DetailResCenter> {

    private final DetailResCenterFragmentView fragmentView;

    public GetResCenterDetailSubscriber(DetailResCenterFragmentView fragmentView) {
        this.fragmentView = fragmentView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        for (int i = 0; i < e.getStackTrace().length; i++) {
            StackTraceElement element = e.getStackTrace()[i];
            Logger.dump(this.getClass().getSimpleName(), element.toString());
        }
        if (e instanceof IOException) {
            fragmentView.setViewData(mappingTimeOutViewModel());
            fragmentView.doOnInitTimeOut();
        } else {
            fragmentView.setViewData(mappingDefaultErrorViewModel());
            fragmentView.doOnInitFailed();
        }
    }

    @Override
    public void onNext(DetailResCenter detailResCenter) {
        fragmentView.setViewData(mappingViewModel(detailResCenter));
        fragmentView.doOnInitSuccess();
    }


    private DetailViewModel mappingDefaultErrorViewModel() {
        return mappingTimeOutViewModel();
    }

    private DetailViewModel mappingTimeOutViewModel() {
        DetailViewModel model = new DetailViewModel();
        model.setSuccess(false);
        model.setTimeOut(true);
        return model;
    }

    private DetailViewModel mappingViewModel(DetailResCenter detailResCenter) {
        DetailViewModel model = new DetailViewModel();
        if (detailResCenter != null && detailResCenter.isSuccess()) {
            model.setSuccess(true);
            model.setAddressReturData(detailResCenter.getAddress() != null ?
                    mappingAddressReturData(detailResCenter.getAddress()) : null
            );
            model.setAwbData(detailResCenter.getShipping() != null ?
                    mappingAwbReturData(detailResCenter.getShipping()) : null
            );
            model.setButtonData(detailResCenter.getButton() != null ?
                    mappingButtonData(detailResCenter.getButton()) : null
            );
            model.setDetailData(detailResCenter.getResolution() != null ?
                    mappingDetailData(detailResCenter.getResolution()) : null
            );
            model.setHistoryData(detailResCenter.getResolutionHistory() != null ?
                    mappingHistoryData(detailResCenter.getResolutionHistory()) : null
            );
            model.setProductData(detailResCenter.getProductData() != null ?
                    mappingProductData(detailResCenter.getProductData()) : null
            );
            model.setSolutionData(detailResCenter.getSolutionData() != null ?
                    mappingSolutionData(detailResCenter.getSolutionData()) : null
            );
            model.setStatusData(detailResCenter.getResolution() != null ?
                    mappingStatusData(detailResCenter.getResolution()) : null
            );
            model.setNextActionDomain(detailResCenter.getNextAction());
        } else {
            model.setSuccess(false);
            model.setMessageError(detailResCenter != null ? detailResCenter.getMessageError() : null);
        }
        return model;
    }

    private AddressReturData mappingAddressReturData(AddressDomainModel domainModel) {
        AddressReturData data = new AddressReturData();
        data.setAddressText("<b>" + domainModel.getReceiver() + "</b>" + "<br>" +
                domainModel.getStreet() + "<br>" +
                domainModel.getDistrict() + ", " + domainModel.getCity() + "<br>" +
                domainModel.getProvince() + "<br>" +
                domainModel.getPostalCode() + "<br>" +
                domainModel.getPhoneReceiver()
        );
        data.setAddressReturDate(domainModel.getDate());
        data.setConversationID(domainModel.getConversationID());
        data.setAddressID(domainModel.getAddressID());
        return data;
    }

    private AwbData mappingAwbReturData(ShippingDomainModel domainModel) {
        AwbData data = new AwbData();
        data.setShipmentRef(domainModel.getShipmentRef());
        data.setAwbDate(domainModel.getShipmentDate());
        data.setShipmentID(domainModel.getShipmentID());
        data.setAttachments(
                domainModel.getAttachment() == null || domainModel.getAttachment().isEmpty() ?
                        null : mappingShippingAttacment(domainModel.getAttachment())
        );
        return data;
    }

    private List<AwbAttachmentViewModel> mappingShippingAttacment(List<AwbAttachmentDomainModel> attachmentList) {
        List<AwbAttachmentViewModel> viewModels = new ArrayList<>();
        for (AwbAttachmentDomainModel items : attachmentList) {
            AwbAttachmentViewModel viewModel = new AwbAttachmentViewModel();
            viewModel.setImageUrl(items.getImageUrl());
            viewModel.setImageThumbUrl(items.getImageThumbUrl());
            viewModels.add(viewModel);
        }
        return viewModels;
    }

    private ButtonData mappingButtonData(ButtonDomainModel domainModel) {
        ButtonData data = new ButtonData();
        data.setShowAskHelp(domainModel.getReport() == 1);
        data.setAskHelpDialogText(domainModel.getReportDialogText());
        data.setShowCancel(domainModel.getCancel() == 1);
        data.setCancelDialogText(domainModel.getCancelDialogText());
        data.setShowEdit(domainModel.getEdit() == 1);
        data.setShowInputAddress(domainModel.getInputAddress() == 1);
        data.setShowAppealSolution(domainModel.getAppeal() == 1);
        data.setShowInputAwb(domainModel.getInputAwb() == 1);
        data.setShowAcceptProduct(domainModel.getFinish() == 1);
        data.setShowAcceptSolution(domainModel.getAccept() == 1);
        data.setShowAcceptAdminSolution(domainModel.getAcceptByAdmin() == 1);
        data.setAcceptProductDialogText(domainModel.getAcceptProductDialogText());
        data.setAcceptReturSolution(domainModel.getAcceptRetur() == 1);
        return data;
    }

    private DetailData mappingDetailData(ResolutionDomainModel domainModel) {
        DetailData data = new DetailData();
        data.setOrderID(domainModel.getOrderID());
        data.setAwbNumber(domainModel.getOrderAwbNumber());
        data.setBuyerID(domainModel.getBuyerID());
        data.setBuyerName(domainModel.getBuyerName());
        data.setComplaintDate(domainModel.getComplaintDate());
        data.setInvoice(domainModel.getInvoice());
        data.setInvoiceUrl(domainModel.getInvoiceUrl());
        data.setResponseDeadline(domainModel.getResponseDeadline());
        data.setDeadlineVisibility(domainModel.isDeadlineVisibility());
        data.setShopID(domainModel.getShopID());
        data.setShopName(domainModel.getShopName());
        data.setReceived(domainModel.getReceivedFlag() == 1);
        data.setFinish(domainModel.getStatusResolutionCode() == 500);
        data.setCancel(domainModel.getStatusResolutionCode() == 0);
        return data;
    }

    private HistoryData mappingHistoryData(ResolutionHistoryDomainModel domainModel) {
        HistoryData data = new HistoryData();
        List<HistoryItem> viewModels = new ArrayList<>();
        for (int i = 0; i < domainModel.getList().size(); i++) {
            ResolutionHistoryItemDomainModel item = domainModel.getList().get(i);
            HistoryItem viewModel = new HistoryItem();
            viewModel.setHistoryText(item.getRemark());
            viewModel.setDate(item.getDate());
            viewModel.setProvider(item.getActionBy());
            viewModel.setLatest(i == 0);
            viewModels.add(viewModel);
        }
        data.setHistoryList(viewModels);
        return data;
    }

    private ProductData mappingProductData(ProductDataDomainModel domainModel) {
        ProductData data = new ProductData();
        List<ProductItem> viewModels = new ArrayList<>();
        for (ProductComplainedDomainModel item : domainModel.getList()) {
            ProductItem viewModel = new ProductItem();
            viewModel.setProductName(item.getProductName());
            viewModel.setProductImageUrl(item.getProductImageUrl());
            viewModel.setProductID(item.getProductID());
            viewModels.add(viewModel);
        }
        data.setProductList(viewModels);
        return data;
    }

    private SolutionData mappingSolutionData(SolutionDomainModel domainModel) {
        SolutionData data = new SolutionData();
        data.setSolutionDate(domainModel.getSolutionDate());
        data.setSolutionProvider(domainModel.getSolutionActionBy());
        data.setEditAble(domainModel.isSolutionEditAble());
        data.setSolutionText(domainModel.getSolutionRemark());
        return data;
    }

    private StatusData mappingStatusData(ResolutionDomainModel domainModel) {
        StatusData data = new StatusData();
        data.setStatusText(domainModel.getStatus());
        return data;
    }
}
