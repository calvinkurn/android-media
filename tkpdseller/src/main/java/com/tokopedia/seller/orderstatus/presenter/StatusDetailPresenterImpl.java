package com.tokopedia.seller.orderstatus.presenter;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.orderstatus.fragment.ShopStatusDetailView;
import com.tokopedia.seller.orderstatus.interactor.ShippingStatusDetailInteractor;
import com.tokopedia.seller.orderstatus.interactor.ShippingStatusDetailInteractorImpl;
import com.tokopedia.seller.orderstatus.model.InvoiceModel;
import com.tokopedia.seller.selling.model.orderShipping.OrderDestination;
import com.tokopedia.seller.selling.model.orderShipping.OrderDetail;
import com.tokopedia.seller.selling.model.orderShipping.OrderShippingList;
import com.tokopedia.seller.selling.model.orderShipping.OrderShop;

import java.util.Map;

/**
 * Created by kris on 1/25/17. Tokopedia
 */

public class StatusDetailPresenterImpl implements StatusDetailPresenter {

    private static final String ORDER_ID = "order_id";
    private static final String SHIPPING_REF = "shipping_ref";
    private OrderShippingList orderData;

    private ShopStatusDetailView viewListener;

    private InvoiceModel invoiceModel;

    private ShippingStatusDetailInteractor interactor;

    private static final String TITLE_PHONE_TOKOPEDIA = "Telepon Tokopedia : ";

    private static final String TITLE_PHONE = "Telepon : ";

    public StatusDetailPresenterImpl(ShopStatusDetailView viewListener) {
        this.viewListener = viewListener;
        interactor = new ShippingStatusDetailInteractorImpl();
        invoiceModel = new InvoiceModel();
    }

    @Override
    public void setOrderDataToInvoiceModel(OrderShippingList orderData) {
        this.orderData = orderData;
        invoiceModel.setOrderId(orderData.getOrderDetail().getDetailOrderId().toString());
        invoiceModel.setUserId(orderData.getOrderCustomer().getCustomerId());
        invoiceModel.setInvoicePdf(orderData.getOrderDetail().getDetailPdf());
        invoiceModel.setInvoiceUrl(orderData.getOrderDetail().getDetailPdfUri());
        invoiceModel.setStatusList(orderData.getOrderHistory());
        invoiceModel.setReferenceNumber(orderData.getOrderDetail().getDetailShipRefNum());
    }

    @Override
    public void setOrderDataToView() {
        viewListener.setPaymentData(orderData.getOrderPayment());
        viewListener.setOrderDetailData(orderData.getOrderDetail());
        viewListener.setCustomerDataToView(orderData.getOrderCustomer());
        viewListener.setShipmentDetailToView(orderData.getOrderShipment());
        viewListener.setRefNum(invoiceModel.getReferenceNumber());
        viewListener.setDeliveryLocationDetail();
        viewListener.setPickUpAddressToView(generatePickupAddress(orderData.getOrderShop()));
        initiateButtonVisibility();
        setOrderStatus();
    }

    @Override
    public InvoiceModel getInvoiceData() {
        return invoiceModel;
    }

    @Override
    public void setPermission(String permission) {
        invoiceModel.setPermission(permission);
    }

    @Override
    public String generatedDestinationString() {
        OrderDestination destination = orderData.getOrderDestination();
        String phoneTokopedia;
        String compiledDestinationString;
        if (destination.getReceiverPhoneIsTokopedia() == 1) {
            phoneTokopedia = TITLE_PHONE_TOKOPEDIA + destination.getReceiverPhone();
        } else {
            phoneTokopedia = TITLE_PHONE + destination.getReceiverPhone();
        }

        compiledDestinationString = MethodChecker
                .fromHtml(destination.getReceiverName()).toString()
                + "\n" + MethodChecker
                .fromHtml(destination.getAddressStreet()
                        .replace("<br/>", "\n").replace("<br>", "\n")).toString()
                + "\n" + destination.getAddressDistrict()
                + " " + destination.getAddressCity()
                + ", " + destination.getAddressPostal()
                + "\n" + destination.getAddressProvince() + "\n" + phoneTokopedia;
        compiledDestinationString = compiledDestinationString.replace("&#39;", "'");
        return compiledDestinationString;
    }

    @Override
    public OrderShippingList getOrderData() {
        return orderData;
    }

    @Override
    public void editRefNumber(Context context, String refNumber) {
        Map<String, String> editRefParams = new TKPDMapParam<>();
        editRefParams.put(ORDER_ID, orderData.getOrderDetail().getDetailOrderId().toString());
        editRefParams.put(SHIPPING_REF, refNumber);
        viewListener.showProgress();
        interactor.editRefNum(context,
                editRefParams,
                new ShippingStatusDetailInteractor.onEditRefNumListener() {
                    @Override
                    public void onSuccess(String refNum) {
                        orderData.getOrderDetail().setDetailShipRefNum(refNum);
                        invoiceModel.setReferenceNumber(refNum);
                        viewListener.setRefNum(refNum);
                        viewListener.hideProgress();
                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        viewListener.showSnackbarError(errorMsg);
                        viewListener.hideProgress();
                    }
                });
    }

    @Override
    public void retryPickup(Context context) {
        TKPDMapParam<String, String> retryPickUpParams = new TKPDMapParam<>();
        retryPickUpParams.put(ORDER_ID, orderData.getOrderDetail().getDetailOrderId().toString());
        viewListener.showProgress();
        interactor.retryCourierPickUp(context,
                retryPickUpParams,
                new ShippingStatusDetailInteractor.onRetryPickupListener() {
                    @Override
                    public void onSuccess(String successMessage) {
                        viewListener.hideProgress();
                        viewListener.showInfoSnackbar(successMessage);
                        viewListener.hideRetryPickUpButton();
                    }

                    @Override
                    public void onFailed(String errorMessage) {
                        viewListener.hideProgress();
                        viewListener.showSnackbarError(errorMessage);
                    }

                    @Override
                    public void noConnection() {
                        viewListener.showNoConnectionError();
                    }
                });
    }

    @Override
    public void onViewDestroyed() {
        interactor.onViewDestroyed();
    }

    private String generatePickupAddress(OrderShop shop) {
        return MethodChecker.fromHtml(shop.getAddressStreet())
                + "\n" + MethodChecker.fromHtml(shop.getAddressCity()).toString()
                + ", " + MethodChecker.fromHtml(shop.getAddressPostal())
                + "\n" + shop.getAddressProvince()
                + TITLE_PHONE + shop.getShipperPhone();
    }

    private void initiateButtonVisibility() {
        viewListener.hideEditRefNum();
        viewListener.hideTrackButton();
        setPickUpButtonVisibility();
        setRetryButtonVisibility();
        setDefaultCourierElementsVisibility();
    }

    private void setDefaultCourierElementsVisibility() {
        if (validatingOrderData() && viewListener.getRefNumber().length() > 0) {
            viewListener.showTrackButton();
            if (orderData.getIsPickUp() == 1) {
                viewListener.hideEditRefNum();
            } else {
                viewListener.showEditRefNum();
            }
        }
        if (invoiceModel.getPermission().equals("0")) {
            viewListener.hideEditRefNum();
            viewListener.hideTrackButton();
        }
    }

    private boolean validatingOrderData() {
        OrderDetail orderDetail = orderData.getOrderDetail();
        return (orderDetail.getDetailOrderStatus() == 500
                || orderDetail.getDetailOrderStatus() == 501
                || orderDetail.getDetailOrderStatus() == 520
                || orderDetail.getDetailOrderStatus() == 530);
    }

    private void setPickUpButtonVisibility() {
        if (orderData.getIsPickUp() == 1) viewListener.showPickUpVisibility();
        else viewListener.hidePickUpVisibility();
    }

    private void setRetryButtonVisibility() {
        if (orderData.getIsAllowedRetry() == 1) {
            viewListener.showRetryPickUpButton();
        } else {
            viewListener.hideRetryPickUpButton();
        }
    }

    private void setOrderStatus() {
        viewListener.removesOrderStatusLayoutView();
        for (int i = 0; (i < invoiceModel.getStatusList().size() && i < 2); i++) {
            viewListener.addOrderStatusView(invoiceModel.getStatusList().get(i));
        }
    }
}
