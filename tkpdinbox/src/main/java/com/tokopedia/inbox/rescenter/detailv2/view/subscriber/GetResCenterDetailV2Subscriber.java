package com.tokopedia.inbox.rescenter.detailv2.view.subscriber;

import android.util.Log;

import com.tkpd.library.utils.Logger;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.AddressReturData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.AttachmentData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.AwbAttachmentViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.AwbData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.ButtonData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.ButtonViewItem;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.DetailData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.DetailViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.FreeReturnData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.HistoryData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.HistoryItem;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.ProductData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.ProductItem;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.ProveData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.SolutionData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.StatusData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.AddressData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.AttachmentDataDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.AttachmentUserData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.ComplainedProductData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.CustomerData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.DetailResponseData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.FirstData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.LastData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.LastSolutionData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.LogData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.OrderData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.ResolutionData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.SellerAddressData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.ShopData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.UserAwbData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ButtonDomain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by yfsx
 */

public class GetResCenterDetailV2Subscriber extends rx.Subscriber<DetailResponseData> {

    public static final int ACTION_BY_BUYER = 1;
    public static final int ACTION_BY_SELLER = 2;
    public static final int ACTION_BY_ADMIN = 3;
    public static final int ACTION_BY_SYSTEM = 4;
    public static final String BUYER = "Pembeli";
    public static final String SELLER = "Penjual";
    public static final String ADMIN = "Admin";
    public static final String SYSTEM = "Sistem";

    public static final String BUTTON_FINISH_COMPLAINT = "button_finish_complaint";
    public static final String BUTTON_ACCEPT_SOLUTION = "button_accept_solution";
    public static final String BUTTON_CHANGE_SOLUTION = "button_change_solution";
    public static final String BUTTON_APPEAL_SOLUTION = "button_appeal_solution";
    public static final String BUTTON_INPUT_ADDRESS = "button_input_address";
    public static final String BUTTON_INPUT_AWB = "button_input_awb";
    public static final String BUTTON_RECOMPLAINT = "button_recomplaint";
    public static final String BUTTON_REPORT = "button_report";
    public static final String BUTTON_CANCEL = "button_cancel";

    private final DetailResCenterFragmentView fragmentView;

    public GetResCenterDetailV2Subscriber(DetailResCenterFragmentView fragmentView) {
        this.fragmentView = fragmentView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Log.d(this.getClass().getSimpleName(), e.getMessage());
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
    public void onNext(DetailResponseData detailResponseData) {
        fragmentView.setViewData(mappingViewModel(detailResponseData));
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

    private DetailViewModel mappingViewModel(DetailResponseData detailResponseData) {
        DetailViewModel viewModel = new DetailViewModel();
        viewModel.setSuccess(true);
        viewModel.setNextActionDomain(detailResponseData.getNextAction());
        viewModel.setAddressReturData(detailResponseData.getLast().getSellerAddress() != null ?
                mappingAddressReturData(detailResponseData.getLast().getSellerAddress()) :
                null);
        viewModel.setAwbData(detailResponseData.getLast().getUserAwb() != null ?
                mappingAwbData(detailResponseData.getLast().getUserAwb(), detailResponseData.getButton()) :
                null);
        viewModel.setButtonData(mappingButtonData(detailResponseData.getButton(),
                detailResponseData.getResolution().getStatus().getId()));
        viewModel.setDetailData(mappingDetailData(detailResponseData));
        viewModel.setHistoryData(detailResponseData.getLogs() != null ?
                mappingHistoryData(detailResponseData.getLogs()) :
                null);
        viewModel.setProductData(detailResponseData.getLast().getComplainedProducts() != null ?
                mappingProductData(detailResponseData.getLast().getComplainedProducts()) :
                null);
        viewModel.setSolutionData(detailResponseData.getLast().getSolution() != null ?
                mappingSolutionData(
                        detailResponseData.getLast().getSolution(),
                        detailResponseData.getButton(),
                        detailResponseData.getLast().getProblem()) :
                null);
        viewModel.setProveData(detailResponseData.getFirst() != null ?
                mappingProveData(detailResponseData.getFirst(), detailResponseData.getAttachments()) :
                null);
        viewModel.setStatusData(mappingStatusData(detailResponseData.getLast()));
        viewModel.setFreeReturnData(mappingFreeReturn(detailResponseData.getResolution()));
        return viewModel;
    }

    private FreeReturnData mappingFreeReturn(ResolutionData data) {
        return new FreeReturnData(
                data.getFreeReturn() == 1,
                data.getFreeReturnText(),
                data.getFreeReturnLink());
    }

    private ProveData mappingProveData(FirstData firstData, List<AttachmentUserData> dataList) {
        ProveData proveData = new ProveData();
        proveData.setRemark(firstData.getBuyerRemark());
        if (dataList != null && dataList.size() != 0) {
            for (AttachmentUserData data : dataList) {
                if (data.getActionBy() == ACTION_BY_BUYER) {
                    proveData.setBuyerAttachmentList(mappingAttachmentData(data.getAttachments()));
                } else if (data.getActionBy() == ACTION_BY_SELLER) {
                    proveData.setSellerAttachmentList(mappingAttachmentData(data.getAttachments()));
                } else if (data.getActionBy() == ACTION_BY_ADMIN) {
                    proveData.setAdminAttachmentList(mappingAttachmentData(data.getAttachments()));
                }
            }
        }
        if ((dataList != null && dataList.size() != 0)
                || (firstData.getBuyerRemark() != null
                && !firstData.getBuyerRemark().isEmpty())) {
            proveData.setCanShowProveData(true);
        } else {
            proveData.setCanShowProveData(false);
        }
        return proveData;
    }

    private List<AttachmentData> mappingAttachmentData(List<AttachmentDataDomain> dataDomains) {
        List<AttachmentData> attachmentDataList = new ArrayList<>();
        for (AttachmentDataDomain dataDomain : dataDomains) {
            AttachmentData attachmentData = new AttachmentData();
            attachmentData.setImageThumbUrl(dataDomain.getThumbnail());
            attachmentData.setImageUrl(dataDomain.getFullUrl());
            attachmentData.setIsVideo(dataDomain.getIsVideo());
            attachmentDataList.add(attachmentData);
        }
        return attachmentDataList;
    }

    private ButtonData mappingButtonData(ButtonDomain domainModel, int resolutionStatusId) {
        ButtonData data = new ButtonData();
        data.setShowAskHelp(domainModel.getReport() == 1);
        data.setAskHelpDialogText(domainModel.getReportText());
        data.setShowCancel(domainModel.getCancel() == 1);
        data.setAcceptTextLite(domainModel.getAcceptTextLite());
        data.setCancelDialogText(domainModel.getCancelText());
        data.setShowEdit(domainModel.getEdit() == 1);
        data.setShowInputAddress(domainModel.getInputAddress() == 1);
        data.setShowAppealSolution(domainModel.getAppeal() == 1);
        data.setShowInputAwb(domainModel.getInputAWB() == 1);
        data.setShowAcceptProduct(domainModel.getFinish() == 1);
        data.setShowAcceptSolution(domainModel.getAccept() == 1);
        data.setShowAcceptAdminSolution(domainModel.getAcceptByAdmin() == 1);
        data.setAcceptProductDialogText(domainModel.getAcceptText());
        data.setFinishComplaintLabel(domainModel.getFinishLabel());
        data.setFinishComplaintDialogText(domainModel.getFinishText());
        data.setAcceptReturSolution(domainModel.getAcceptReturn() == 1);
        data.setAskHelpLabel(domainModel.getReportLabel());
        data.setCancelLabel(domainModel.getCancelLabel());
        data.setAcceptLabel(domainModel.getAcceptLabel());
        data.setEditLabel(domainModel.getEditLabel());
        data.setInputAddressLabel(domainModel.getInputAddressLabel());
        data.setAppealLabel(domainModel.getAppealLabel());
        data.setInputAwbLabel(domainModel.getInputAWBLabel());
        data.setButtonViewItemList(mappingButtonViewItem(domainModel));
        data.setResolutionStatus(resolutionStatusId);
        data.setCancelOn4thOrder(domainModel.getCancelOrder() == 4);
        return data;
    }

    private StatusData mappingStatusData(LastData lastData) {
        StatusData statusData = new StatusData();
        statusData.setStatusText(lastData.getStatus());
        return statusData;
    }

    private SolutionData mappingSolutionData(LastSolutionData lastSolutionData, ButtonDomain buttonDomain, String problem) {
        SolutionData solutionData = new SolutionData();
        solutionData.setSolutionDate(lastSolutionData.getCreateTimeStr());
        solutionData.setSolutionProvider(String.valueOf(lastSolutionData.getActionBy()));
        solutionData.setSolutionProviderName(mappingSolutionProvider(lastSolutionData.getActionBy()));
        solutionData.setSolutionText(lastSolutionData.getNameCustom());
        solutionData.setEditAble(buttonDomain.getEdit() == 1);
        solutionData.setSolutionProblem(problem);
        return solutionData;
    }

    private String mappingSolutionProvider(int actionBy) {
        if (actionBy == ACTION_BY_BUYER) {
            return BUYER;
        } else if (actionBy == ACTION_BY_SELLER) {
            return SELLER;
        } else if (actionBy == ACTION_BY_ADMIN) {
            return ADMIN;
        } else {
            return SYSTEM;
        }
    }

    private ProductData mappingProductData(List<ComplainedProductData> complainedProductDataList) {
        ProductData productData = new ProductData();
        List<ProductItem> productItems = new ArrayList<>();
        for (ComplainedProductData complainedProductData : complainedProductDataList) {
            if (complainedProductData.getProduct() != null) {
                ProductItem productItem = new ProductItem();
                productItem.setProductID(String.valueOf(complainedProductData.getId()));
                productItem.setProductImageUrl(complainedProductData.getProduct().getThumb());
                productItem.setProductName(complainedProductData.getProduct().getName());
                productItems.add(productItem);
            }
        }
        productData.setProductList(productItems);
        return productData;
    }

    private HistoryData mappingHistoryData(List<LogData> logDataList) {
        HistoryData historyData = new HistoryData();
        List<HistoryItem> historyItems = new ArrayList<>();
        int pos = 0;
        for (LogData logData : logDataList) {
            HistoryItem item = new HistoryItem();
            item.setLatest(pos == logDataList.size() - 1);
            item.setDate(logData.getCreateTimestampStr());
            item.setDateNumber(logData.getDateNumber());
            item.setMonth(logData.getMonth());
            item.setHistoryText(logData.getAction());
            item.setProvider(logData.getActionBy().getName());
            item.setDateTimestamp(logData.getCreateTimestampStr());
            item.setProviderId(logData.getActionBy().getId());
            item.setTimeNumber(logData.getTimeNumber());
            historyItems.add(item);
            pos++;
        }
        historyData.setHistoryList(historyItems);
        return historyData;
    }

    private DetailData mappingDetailData(DetailResponseData detailResponseData) {
        DetailData data = new DetailData();
        OrderData orderData = detailResponseData.getOrder();
        ResolutionData resolutionData = detailResponseData.getResolution();
        CustomerData customerData = detailResponseData.getCustomer();
        ShopData shopData = detailResponseData.getShop();
        LastSolutionData lastSolutionData = detailResponseData.getLast().getSolution();
        data.setOrderID(String.valueOf(orderData.getId()));
        data.setAwbNumber(orderData.getAwb());
        data.setBuyerID(String.valueOf(customerData.getId()));
        data.setBuyerName(customerData.getName());
        data.setComplaintDate(resolutionData.getCreateTimeStr());
        data.setComplaintDateTimestamp(resolutionData.getCreateTimeStr());
        data.setInvoice(orderData.getInvoice().getRefNum());
        data.setInvoiceUrl(orderData.getInvoice().getUrl());
        data.setResponseDeadline(resolutionData.getExpireTimeStr());
        data.setDeadlineVisibility(resolutionData.getExpireTimeStr() != null);
        data.setShopID(String.valueOf(shopData.getId()));
        data.setShopName(shopData.getName());
        data.setReceived(lastSolutionData.getReceivedFlag() == 1);
        data.setFinish(resolutionData.getStatus().getId() == 500);
        data.setCancel(resolutionData.getStatus().getId() == 0);
        data.setCanAskHelp(detailResponseData.getButton().getReport() == 1);
        data.setResolutionStatus(resolutionData.getStatus().getId());
        return data;
    }

    private AwbData mappingAwbData(UserAwbData userAwbData, ButtonDomain domainModel) {
        AwbData awbData = new AwbData();
        awbData.setShipmentID(String.valueOf(userAwbData.getShipping().getId()));
        awbData.setShipmentRef(userAwbData.getAwb());
        awbData.setShipmentName(userAwbData.getShipping().getName());
        awbData.setAwbDateTimestamp(userAwbData.getCreateTimeFullStr());
        awbData.setAwbDate(userAwbData.getCreateTimeStr());
        awbData.setAttachments(mappingAwbAttachments(userAwbData.getAttachments()));
        awbData.setAddButtonAvailable(domainModel.getInputAWB() == 1);
        return awbData;
    }

    private List<AwbAttachmentViewModel> mappingAwbAttachments(List<AttachmentDataDomain> attachmentDataList) {
        List<AwbAttachmentViewModel> attachmentViewModels = new ArrayList<>();
        if(attachmentDataList!= null)
        for (AttachmentDataDomain attachmentData : attachmentDataList) {
            AwbAttachmentViewModel awbAttachmentViewModel = new AwbAttachmentViewModel();
            awbAttachmentViewModel.setImageThumbUrl(attachmentData.getThumbnail());
            awbAttachmentViewModel.setImageUrl(attachmentData.getFullUrl());
            attachmentViewModels.add(awbAttachmentViewModel);
        }
        return attachmentViewModels;
    }

    private AddressReturData mappingAddressReturData(SellerAddressData sellerAddressData) {
        AddressReturData addressReturData = new AddressReturData();
        AddressData addressData = sellerAddressData.getAddress();
        addressReturData.setAddressID(String.valueOf(addressData.getAddressId()));
        addressReturData.setAddressReturDate(sellerAddressData.getCreateTimeStr());
        addressReturData.setAddressReturDateTimestamp(sellerAddressData.getCreateTimeFullStr());
        addressReturData.setAddressText(getAddressFormat(sellerAddressData.getAddress()));
        addressReturData.setConversationID(String.valueOf(sellerAddressData.getConversationId()));
        return addressReturData;
    }

    private List<ButtonViewItem> mappingButtonViewItem(ButtonDomain domainModel) {
        List<ButtonViewItem> itemList = new ArrayList<>();
        boolean isFinishInserted = false;
        if (domainModel.getFinish() != 0) {
            ButtonViewItem data = new ButtonViewItem(
                    domainModel.getFinishLabel(),
                    BUTTON_FINISH_COMPLAINT,
                    domainModel.getFinishOrder());
            isFinishInserted = true;
            itemList.add(data);
        }
        if (domainModel.getAccept() != 0 && !isFinishInserted) {
            ButtonViewItem data = new ButtonViewItem(
                    domainModel.getAcceptLabel(),
                    BUTTON_ACCEPT_SOLUTION,
                    domainModel.getAcceptOrder());
            itemList.add(data);
        }
        if (domainModel.getEdit() != 0) {
            ButtonViewItem data = new ButtonViewItem(
                    domainModel.getEditLabel(),
                    BUTTON_CHANGE_SOLUTION,
                    domainModel.getEditOrder());
            itemList.add(data);
        }
        if (domainModel.getAppeal() != 0) {
            ButtonViewItem data = new ButtonViewItem(
                    domainModel.getAppealLabel(),
                    BUTTON_APPEAL_SOLUTION,
                    domainModel.getAppealOrder());
            itemList.add(data);
        }
        if (domainModel.getInputAddress() != 0) {
            ButtonViewItem data = new ButtonViewItem(
                    domainModel.getInputAddressLabel(),
                    BUTTON_INPUT_ADDRESS,
                    domainModel.getInputAddressOrder());
            itemList.add(data);
        }
        if (domainModel.getInputAWB() != 0) {
            ButtonViewItem data = new ButtonViewItem(
                    domainModel.getInputAWBLabel(),
                    BUTTON_INPUT_AWB,
                    domainModel.getInputAWBOrder());
            itemList.add(data);
        }
        if (domainModel.getRecomplaint() != 0) {
            ButtonViewItem data = new ButtonViewItem(
                    domainModel.getRecomplainLabel(),
                    BUTTON_RECOMPLAINT,
                    domainModel.getRecomplaintOrder());
            itemList.add(data);
        }
        if (domainModel.getReport() != 0) {
            ButtonViewItem data = new ButtonViewItem(
                    domainModel.getReportLabel(),
                    BUTTON_REPORT,
                    domainModel.getReportOrder());
            itemList.add(data);
        }
        if (domainModel.getCancel() != 0 && domainModel.getCancelOrder() != 4) {
            ButtonViewItem data = new ButtonViewItem(
                    domainModel.getCancelLabel(),
                    BUTTON_CANCEL,
                    domainModel.getCancelOrder());
            itemList.add(data);
        }
        Collections.sort(itemList, new ButtonOrderComparator());
        return itemList;
    }


    private class ButtonOrderComparator implements Comparator<ButtonViewItem> {
        @Override
        public int compare(ButtonViewItem button1, ButtonViewItem button2) {
            return button2.getOrder() - button1.getOrder();
        }
    }

    private String getAddressFormat(AddressData domainModel) {
        return "<b>" + domainModel.getReceiver() + "</b>" + "<br>" + "<br>" +
                domainModel.getAddress() + "<br>" +
                domainModel.getDistrict() + ", " + domainModel.getCity()  + " - " +
                domainModel.getPostalCode() + "<br>" +
                domainModel.getProvince() + "<br>" +
                "Telp: " + domainModel.getPhone();
    }
}
