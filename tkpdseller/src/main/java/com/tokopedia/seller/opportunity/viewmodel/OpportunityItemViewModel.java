package com.tokopedia.seller.opportunity.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.util.MethodChecker;

import java.util.List;

/**
 * Created by nisie on 3/1/17.
 */

public class OpportunityItemViewModel implements Parcelable {

    private int orderJOBStatus;
    private int orderIsPickup;
    private int orderShippingRetry;
    private OrderCustomerViewModel orderCustomer;
    private Object orderPayment;
    private OrderDetailViewModel orderDetail;
    private String orderAutoResi;
    private OrderDeadlineViewModel orderDeadline;
    private int orderAutoAwb;
    private OrderShopViewModel orderShop;
    private List<OrderProductViewModel> orderProducts = null;
    private OrderShipmentViewModel orderShipment;
    private Object orderLast;
    private Object orderHistory;
    private Object orderJOBDetail;
    private OrderDestinationViewModel orderDestination;

    //TODO : Delete later
    private String productName;
    private String productPrice;
    private String productImage;
    private String deadline;

    public OpportunityItemViewModel(String productName) {
        this.productName = productName;
        this.productPrice = "Rp 10.000.000";
        this.deadline = "24 jam lagi";
        this.productImage = "https://pbs.twimg.com/media/C51fHIHVAAQH2WZ.jpg";
    }

    protected OpportunityItemViewModel(Parcel in) {
        orderJOBStatus = in.readInt();
        orderIsPickup = in.readInt();
        orderShippingRetry = in.readInt();
        orderAutoResi = in.readString();
        orderAutoAwb = in.readInt();
        productName = in.readString();
        productPrice = in.readString();
        productImage = in.readString();
        deadline = in.readString();
    }

    public static final Creator<OpportunityItemViewModel> CREATOR = new Creator<OpportunityItemViewModel>() {
        @Override
        public OpportunityItemViewModel createFromParcel(Parcel in) {
            return new OpportunityItemViewModel(in);
        }

        @Override
        public OpportunityItemViewModel[] newArray(int size) {
            return new OpportunityItemViewModel[size];
        }
    };

    public String getProductName() {
        return MethodChecker.fromHtml(productName).toString();
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(orderJOBStatus);
        parcel.writeInt(orderIsPickup);
        parcel.writeInt(orderShippingRetry);
        parcel.writeString(orderAutoResi);
        parcel.writeInt(orderAutoAwb);
        parcel.writeString(productName);
        parcel.writeString(productPrice);
        parcel.writeString(productImage);
        parcel.writeString(deadline);
    }

}
