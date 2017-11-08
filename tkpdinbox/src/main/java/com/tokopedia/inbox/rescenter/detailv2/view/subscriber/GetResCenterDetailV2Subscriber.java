package com.tokopedia.inbox.rescenter.detailv2.view.subscriber;

import android.util.Log;

import com.tkpd.library.utils.Logger;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.AddressReturData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.AwbAttachmentViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.AwbData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.DetailData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.DetailViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.AddressData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.AttachmentData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.CustomerData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.DetailResponseData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.OrderData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.ResolutionData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.SellerAddressData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.ShopData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.SolutionData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.UserAwbData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 3/16/17.
 */

public class GetResCenterDetailV2Subscriber extends rx.Subscriber<DetailResponseData> {

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
        viewModel.setNextActionDomain(detailResponseData.getNextAction());
        viewModel.setAddressReturData(detailResponseData.getLast() != null ? mappingAddressReturData(detailResponseData.getLast().getSellerAddress()) : null);
        viewModel.setAwbData(detailResponseData.getLast() != null && detailResponseData.getLast().getUserAwb() != null ? mappingAwbData(detailResponseData.getLast().getUserAwb()) : null);
        viewModel.setButtonData(null);
        viewModel.setDetailData(mappingDetailData(detailResponseData));
        viewModel.setHistoryData(null);
        viewModel.setProductData(null);
        viewModel.setSolutionData(null);
        viewModel.setStatusData(null);
        return viewModel;
    }

    private DetailData mappingDetailData(DetailResponseData detailResponseData) {
        DetailData data = new DetailData();
        OrderData orderData = detailResponseData.getOrder();
        ResolutionData resolutionData = detailResponseData.getResolution();
        CustomerData customerData = detailResponseData.getCustomer();
        ShopData shopData = detailResponseData.getShop();
        SolutionData solutionData = detailResponseData.getLast().getSolution();
        data.setOrderID(String.valueOf(orderData.getId()));
        data.setAwbNumber(orderData.getAwb());
        data.setBuyerID(String.valueOf(customerData.getId()));
        data.setBuyerName(customerData.getName());
        data.setComplaintDate(resolutionData.getCreateTimeStr());
        data.setInvoice(orderData.getInvoice().getRefNum());
        data.setInvoiceUrl(orderData.getInvoice().getUrl());
        data.setResponseDeadline(resolutionData.getExpireTimeStr());
        data.setDeadlineVisibility(resolutionData.getExpireTimeStr() != null);
        data.setShopID(String.valueOf(shopData.getId()));
        data.setShopName(shopData.getName());
        data.setReceived(solutionData.getReceivedFlag() == 1);
        data.setFinish(resolutionData.getStatus().getId() == 500);
        data.setCancel(resolutionData.getStatus().getId() == 0);
        return data;
    }

    private AwbData mappingAwbData(UserAwbData userAwbData) {
        AwbData awbData = new AwbData();
        awbData.setShipmentID(String.valueOf(userAwbData.getShipping().getId()));
        awbData.setShipmentRef(userAwbData.getAwb());
        awbData.setAwbDate(userAwbData.getCreateTimeStr());
        awbData.setAttachments(userAwbData.getAttachments() != null ? mappingAttachments(userAwbData.getAttachments()) : null);
        return awbData;
    }

    private List<AwbAttachmentViewModel> mappingAttachments(List<AttachmentData> attachmentDataList) {
        List<AwbAttachmentViewModel> attachmentViewModels = new ArrayList<>();
        for (AttachmentData attachmentData : attachmentDataList) {
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
        String addressTextBuilder = "<b>" + addressData.getReceiver()+ "</b>" + "<br>" +
                addressData.getAddress() + "<br>" +
                addressData.getDistrict() + ", " + addressData.getCity() + "<br>" +
                addressData.getProvince() + "<br>" +
                addressData.getPostalCode() + "<br>" +
                addressData.getPhone();
        addressReturData.setAddressText(addressTextBuilder);
        addressReturData.setConversationID(String.valueOf(sellerAddressData.getConversationId()));
        return addressReturData;
    }

}
