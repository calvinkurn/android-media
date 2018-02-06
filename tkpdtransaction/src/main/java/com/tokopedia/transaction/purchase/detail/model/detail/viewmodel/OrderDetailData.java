package com.tokopedia.transaction.purchase.detail.model.detail.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by kris on 11/2/17. Tokopedia
 */

public class OrderDetailData implements Parcelable{

    private String orderId;

    private String orderCode;

    private String orderStatus;

    private String resoId;

    private String orderImage;

    private String purchaseDate;

    private String responseTimeLimit;

    private String deadlineColorString;

    private boolean isRequestCancel;

    private String requestCancelReason;

    private String shopId;

    private String shopName;

    private String shopLogo;

    private String buyerName;

    private String buyerUserName;

    private String buyerId;

    private String courierName;

    private String shipmentName;

    private String shipmentId;

    private String shipmentServiceName;

    private String shipmentServiceId;

    private String shippingAddress;

    private String awb;

    private String partialOrderStatus;

    private String preorderPeriod;

    private String preorderPeriodText;

    private boolean isPreorder;

    private String dropshipperName;

    private String dropshipperPhone;

    private String invoiceNumber;

    private String invoiceUrl;

    private List<OrderDetailItemData> itemList;

    private String totalItemQuantity;

    private String totalItemWeight;

    private String productPrice;

    private String deliveryPrice;

    private String insurancePrice;

    private String additionalFee;

    private String totalPayment;

    private ButtonData buttonData;

    private String drivername;

    private String driverPhone;

    private String driverImage;

    private String driverVehicle;

    private boolean showInsuranceNotification;

    private String insuranceNotification;

    public OrderDetailData() {

    }

    protected OrderDetailData(Parcel in) {
        orderId = in.readString();
        orderCode = in.readString();
        orderStatus = in.readString();
        resoId = in.readString();
        orderImage = in.readString();
        purchaseDate = in.readString();
        responseTimeLimit = in.readString();
        deadlineColorString = in.readString();
        isRequestCancel = in.readByte() != 0;
        requestCancelReason = in.readString();
        shopId = in.readString();
        shopName = in.readString();
        shopLogo = in.readString();
        buyerName = in.readString();
        buyerUserName = in.readString();
        buyerId = in.readString();
        courierName = in.readString();
        shipmentName = in.readString();
        shipmentId = in.readString();
        shipmentServiceName = in.readString();
        shipmentServiceId = in.readString();
        shippingAddress = in.readString();
        awb = in.readString();
        partialOrderStatus = in.readString();
        preorderPeriod = in.readString();
        preorderPeriodText = in.readString();
        isPreorder = in.readByte() != 0;
        dropshipperName = in.readString();
        dropshipperPhone = in.readString();
        invoiceNumber = in.readString();
        invoiceUrl = in.readString();
        itemList = in.createTypedArrayList(OrderDetailItemData.CREATOR);
        totalItemQuantity = in.readString();
        totalItemWeight = in.readString();
        productPrice = in.readString();
        deliveryPrice = in.readString();
        insurancePrice = in.readString();
        additionalFee = in.readString();
        totalPayment = in.readString();
        buttonData = in.readParcelable(ButtonData.class.getClassLoader());
        drivername = in.readString();
        driverPhone = in.readString();
        driverImage = in.readString();
        driverVehicle = in.readString();
        showInsuranceNotification = in.readByte() != 0;
        insuranceNotification = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderId);
        dest.writeString(orderCode);
        dest.writeString(orderStatus);
        dest.writeString(resoId);
        dest.writeString(orderImage);
        dest.writeString(purchaseDate);
        dest.writeString(responseTimeLimit);
        dest.writeString(deadlineColorString);
        dest.writeByte((byte) (isRequestCancel ? 1 : 0));
        dest.writeString(requestCancelReason);
        dest.writeString(shopId);
        dest.writeString(shopName);
        dest.writeString(shopLogo);
        dest.writeString(buyerName);
        dest.writeString(buyerUserName);
        dest.writeString(buyerId);
        dest.writeString(courierName);
        dest.writeString(shipmentName);
        dest.writeString(shipmentId);
        dest.writeString(shipmentServiceName);
        dest.writeString(shipmentServiceId);
        dest.writeString(shippingAddress);
        dest.writeString(awb);
        dest.writeString(partialOrderStatus);
        dest.writeString(preorderPeriod);
        dest.writeString(preorderPeriodText);
        dest.writeByte((byte) (isPreorder ? 1 : 0));
        dest.writeString(dropshipperName);
        dest.writeString(dropshipperPhone);
        dest.writeString(invoiceNumber);
        dest.writeString(invoiceUrl);
        dest.writeTypedList(itemList);
        dest.writeString(totalItemQuantity);
        dest.writeString(totalItemWeight);
        dest.writeString(productPrice);
        dest.writeString(deliveryPrice);
        dest.writeString(insurancePrice);
        dest.writeString(additionalFee);
        dest.writeString(totalPayment);
        dest.writeParcelable(buttonData, flags);
        dest.writeString(drivername);
        dest.writeString(driverPhone);
        dest.writeString(driverImage);
        dest.writeString(driverVehicle);
        dest.writeByte((byte) (showInsuranceNotification ? 1 : 0));
        dest.writeString(insuranceNotification);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderDetailData> CREATOR = new Creator<OrderDetailData>() {
        @Override
        public OrderDetailData createFromParcel(Parcel in) {
            return new OrderDetailData(in);
        }

        @Override
        public OrderDetailData[] newArray(int size) {
            return new OrderDetailData[size];
        }
    };

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getResoId() {
        return resoId;
    }

    public void setResoId(String resoId) {
        this.resoId = resoId;
    }

    public String getOrderImage() {
        return orderImage;
    }

    public void setOrderImage(String orderImage) {
        this.orderImage = orderImage;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getResponseTimeLimit() {
        return responseTimeLimit;
    }

    public void setResponseTimeLimit(String responseTimeLimit) {
        this.responseTimeLimit = responseTimeLimit;
    }

    public String getDeadlineColorString() {
        return deadlineColorString;
    }

    public void setDeadlineColorString(String deadlineColorString) {
        this.deadlineColorString = deadlineColorString;
    }

    public boolean isRequestCancel() {
        return isRequestCancel;
    }

    public void setRequestCancel(boolean requestCancel) {
        isRequestCancel = requestCancel;
    }

    public String getRequestCancelReason() {
        return requestCancelReason;
    }

    public void setRequestCancelReason(String requestCancelReason) {
        this.requestCancelReason = requestCancelReason;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerUserName() {
        return buyerUserName;
    }

    public void setBuyerUserName(String buyerUserName) {
        this.buyerUserName = buyerUserName;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getShipmentName() {
        return shipmentName;
    }

    public void setShipmentName(String shipmentName) {
        this.shipmentName = shipmentName;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getShipmentServiceName() {
        return shipmentServiceName;
    }

    public void setShipmentServiceName(String shipmentServiceName) {
        this.shipmentServiceName = shipmentServiceName;
    }

    public String getShipmentServiceId() {
        return shipmentServiceId;
    }

    public void setShipmentServiceId(String shipmentServiceId) {
        this.shipmentServiceId = shipmentServiceId;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String ShippingAddress) {
        this.shippingAddress = ShippingAddress;
    }

    public String getAwb() {
        return awb;
    }

    public void setAwb(String awb) {
        this.awb = awb;
    }

    public String getPartialOrderStatus() {
        return partialOrderStatus;
    }

    public void setPartialOrderStatus(String PartialOrderStatus) {
        this.partialOrderStatus = PartialOrderStatus;
    }

    public boolean isPreorder() {
        return isPreorder;
    }

    public void setPreorder(boolean preorder) {
        isPreorder = preorder;
    }

    public String getPreorderPeriod() {
        return preorderPeriod;
    }

    public void setPreorderPeriod(String preorderPeriod) {
        this.preorderPeriod = preorderPeriod;
    }

    public String getPreorderPeriodText() {
        return preorderPeriodText;
    }

    public void setPreorderPeriodText(String preorderPeriodText) {
        this.preorderPeriodText = preorderPeriodText;
    }

    public String getDropshipperName() {
        return dropshipperName;
    }

    public void setDropshipperName(String dropshipperName) {
        this.dropshipperName = dropshipperName;
    }

    public String getDropshipperPhone() {
        return dropshipperPhone;
    }

    public void setDropshipperPhone(String dropshipperPhone) {
        this.dropshipperPhone = dropshipperPhone;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }

    public List<OrderDetailItemData> getItemList() {
        return itemList;
    }

    public void setItemList(List<OrderDetailItemData> itemList) {
        this.itemList = itemList;
    }

    public String getTotalItemQuantity() {
        return totalItemQuantity;
    }

    public void setTotalItemQuantity(String totalItemQuantity) {
        this.totalItemQuantity = totalItemQuantity;
    }

    public String getTotalItemWeight() {
        return totalItemWeight;
    }

    public void setTotalItemWeight(String totalItemWeight) {
        this.totalItemWeight = totalItemWeight;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public String getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(String insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public String getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(String additionalFee) {
        this.additionalFee = additionalFee;
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }

    public ButtonData getButtonData() {
        return buttonData;
    }

    public void setButtonData(ButtonData buttonData) {
        this.buttonData = buttonData;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getDriverName() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public String getDriverVehicle() {
        return driverVehicle;
    }

    public void setDriverVehicle(String driverVehicle) {
        this.driverVehicle = driverVehicle;
    }

    public boolean isShowInsuranceNotification() {
        return showInsuranceNotification;
    }

    public void setShowInsuranceNotification(boolean showInsuranceNotification) {
        this.showInsuranceNotification = showInsuranceNotification;
    }

    public String getInsuranceNotification() {
        return insuranceNotification;
    }

    public void setInsuranceNotification(String insuranceNotification) {
        this.insuranceNotification = insuranceNotification;
    }
}
