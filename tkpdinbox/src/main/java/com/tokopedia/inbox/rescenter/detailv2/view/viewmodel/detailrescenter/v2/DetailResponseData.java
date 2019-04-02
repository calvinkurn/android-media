package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;


import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ButtonDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;

import java.util.List;

/**
 * Created by yfsx on 07/11/17.
 */

public class DetailResponseData implements Parcelable {

    private FirstData first;
    private LastData last;
    private ButtonDomain button;
    private ShopData shop;
    private CustomerData customer;
    private OrderData order;
    private ResolutionData resolution;
    private ActionByData actionBy;
    private NextActionDomain nextAction;
    private List<AttachmentUserData> attachments;
    private List<LogData> logs;
    private boolean isSuccess;
    private String errorMessage;

    public DetailResponseData(FirstData first,
                              LastData last,
                              ButtonDomain button,
                              ShopData shop,
                              CustomerData customer,
                              OrderData order,
                              ResolutionData resolution,
                              ActionByData actionBy,
                              NextActionDomain nextAction,
                              List<AttachmentUserData> attachments,
                              List<LogData> logs) {
        this.first = first;
        this.last = last;
        this.button = button;
        this.shop = shop;
        this.customer = customer;
        this.order = order;
        this.resolution = resolution;
        this.actionBy = actionBy;
        this.nextAction = nextAction;
        this.attachments = attachments;
        this.logs = logs;
    }

    public List<AttachmentUserData> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentUserData> attachments) {
        this.attachments = attachments;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public FirstData getFirst() {
        return first;
    }

    public void setFirst(FirstData first) {
        this.first = first;
    }

    public LastData getLast() {
        return last;
    }

    public void setLast(LastData last) {
        this.last = last;
    }

    public ButtonDomain getButton() {
        return button;
    }

    public void setButton(ButtonDomain button) {
        this.button = button;
    }

    public ShopData getShop() {
        return shop;
    }

    public void setShop(ShopData shop) {
        this.shop = shop;
    }

    public CustomerData getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerData customer) {
        this.customer = customer;
    }

    public OrderData getOrder() {
        return order;
    }

    public void setOrder(OrderData order) {
        this.order = order;
    }

    public ResolutionData getResolution() {
        return resolution;
    }

    public void setResolution(ResolutionData resolution) {
        this.resolution = resolution;
    }

    public ActionByData getActionBy() {
        return actionBy;
    }

    public void setActionBy(ActionByData actionBy) {
        this.actionBy = actionBy;
    }

    public NextActionDomain getNextAction() {
        return nextAction;
    }

    public void setNextAction(NextActionDomain nextAction) {
        this.nextAction = nextAction;
    }

    public List<LogData> getLogs() {
        return logs;
    }

    public void setLogs(List<LogData> logs) {
        this.logs = logs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.first, flags);
        dest.writeParcelable(this.last, flags);
        dest.writeParcelable(this.button, flags);
        dest.writeParcelable(this.shop, flags);
        dest.writeParcelable(this.customer, flags);
        dest.writeParcelable(this.order, flags);
        dest.writeParcelable(this.resolution, flags);
        dest.writeParcelable(this.actionBy, flags);
        dest.writeParcelable(this.nextAction, flags);
        dest.writeTypedList(this.attachments);
        dest.writeTypedList(this.logs);
        dest.writeByte(this.isSuccess ? (byte) 1 : (byte) 0);
        dest.writeString(this.errorMessage);
    }

    protected DetailResponseData(Parcel in) {
        this.first = in.readParcelable(FirstData.class.getClassLoader());
        this.last = in.readParcelable(LastData.class.getClassLoader());
        this.button = in.readParcelable(ButtonDomain.class.getClassLoader());
        this.shop = in.readParcelable(ShopData.class.getClassLoader());
        this.customer = in.readParcelable(CustomerData.class.getClassLoader());
        this.order = in.readParcelable(OrderData.class.getClassLoader());
        this.resolution = in.readParcelable(ResolutionData.class.getClassLoader());
        this.actionBy = in.readParcelable(ActionByData.class.getClassLoader());
        this.nextAction = in.readParcelable(NextActionDomain.class.getClassLoader());
        this.attachments = in.createTypedArrayList(AttachmentUserData.CREATOR);
        this.logs = in.createTypedArrayList(LogData.CREATOR);
        this.isSuccess = in.readByte() != 0;
        this.errorMessage = in.readString();
    }

    public static final Creator<DetailResponseData> CREATOR = new Creator<DetailResponseData>() {
        @Override
        public DetailResponseData createFromParcel(Parcel source) {
            return new DetailResponseData(source);
        }

        @Override
        public DetailResponseData[] newArray(int size) {
            return new DetailResponseData[size];
        }
    };
}
