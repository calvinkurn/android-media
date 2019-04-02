package com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.inbox.rescenter.inboxv2.view.adapter.typefactory.ResoInboxTypeFactory;

import java.util.List;

/**
 * Created by yfsx on 24/01/18.
 */

public class InboxItemViewModel implements Parcelable, Visitable<ResoInboxTypeFactory> {
    private int id;
    private int resId;
    private int actionBy;
    private String inboxMessage;
    private String inboxMessageBackgroundColor;
    private String inboxMessageTextColor;
    private String invoiceNumber;
    private boolean isNotificationShow;
    private String nameTitle;
    private String userName;
    private String autoDoneText;
    private String autoDoneBackgroundColor;
    private String autoDoneTextColor;
    private String lastReplyText;
    private String freeReturnText;
    private List<String> imageList;
    private String extraImageCountText;
    private String customerName;
    private String sellerName;
    private boolean isLoadingItem;

    public InboxItemViewModel(int id,
                              int resId,
                              int actionBy,
                              String inboxMessage,
                              String inboxMessageBackgroundColor,
                              String inboxMessageTextColor,
                              String invoiceNumber,
                              boolean isNotificationShow,
                              String nameTitle,
                              String userName,
                              String autoDoneText,
                              String autoDoneBackgroundColor,
                              String autoDoneTextColor,
                              String lastReplyText,
                              String freeReturnText,
                              List<String> imageList,
                              String extraImageCountText,
                              String customerName,
                              String sellerName) {
        this.id = id;
        this.resId = resId;
        this.actionBy = actionBy;
        this.inboxMessage = inboxMessage;
        this.inboxMessageBackgroundColor = inboxMessageBackgroundColor;
        this.inboxMessageTextColor = inboxMessageTextColor;
        this.invoiceNumber = invoiceNumber;
        this.isNotificationShow = isNotificationShow;
        this.nameTitle = nameTitle;
        this.userName = userName;
        this.autoDoneText = autoDoneText;
        this.autoDoneBackgroundColor = autoDoneBackgroundColor;
        this.autoDoneTextColor = autoDoneTextColor;
        this.lastReplyText = lastReplyText;
        this.freeReturnText = freeReturnText;
        this.imageList = imageList;
        this.extraImageCountText = extraImageCountText;
        this.customerName = customerName;
        this.sellerName = sellerName;
    }

    public void updateItem(InboxItemViewModel updateItem) {
        this.id = updateItem.id;
        this.resId = updateItem.resId;
        this.actionBy = updateItem.actionBy;
        this.inboxMessage = updateItem.inboxMessage;
        this.inboxMessageBackgroundColor = updateItem.inboxMessageBackgroundColor;
        this.inboxMessageTextColor = updateItem.inboxMessageTextColor;
        this.invoiceNumber = updateItem.invoiceNumber;
        this.isNotificationShow = updateItem.isNotificationShow;
        this.nameTitle = updateItem.nameTitle;
        this.userName = updateItem.userName;
        this.autoDoneText = updateItem.autoDoneText;
        this.autoDoneBackgroundColor = updateItem.autoDoneBackgroundColor;
        this.autoDoneTextColor = updateItem.autoDoneTextColor;
        this.lastReplyText = updateItem.lastReplyText;
        this.freeReturnText = updateItem.freeReturnText;
        this.imageList = updateItem.imageList;
        this.extraImageCountText = updateItem.extraImageCountText;
        this.customerName = updateItem.customerName;
        this.sellerName = updateItem.sellerName;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public InboxItemViewModel(boolean isLoadingItem) {
        this.isLoadingItem = isLoadingItem;
    }

    public boolean isLoadingItem() {
        return isLoadingItem;
    }

    public void setLoadingItem(boolean loadingItem) {
        isLoadingItem = loadingItem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActionBy() {
        return actionBy;
    }

    public void setActionBy(int actionBy) {
        this.actionBy = actionBy;
    }

    public String getInboxMessage() {
        return inboxMessage;
    }

    public void setInboxMessage(String inboxMessage) {
        this.inboxMessage = inboxMessage;
    }

    public String getInboxMessageBackgroundColor() {
        return inboxMessageBackgroundColor;
    }

    public void setInboxMessageBackgroundColor(String inboxMessageBackgroundColor) {
        this.inboxMessageBackgroundColor = inboxMessageBackgroundColor;
    }

    public String getInboxMessageTextColor() {
        return inboxMessageTextColor;
    }

    public void setInboxMessageTextColor(String inboxMessageTextColor) {
        this.inboxMessageTextColor = inboxMessageTextColor;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public boolean isNotificationShow() {
        return isNotificationShow;
    }

    public void setNotificationShow(boolean notificationShow) {
        isNotificationShow = notificationShow;
    }

    public String getNameTitle() {
        return nameTitle;
    }

    public void setNameTitle(String nameTitle) {
        this.nameTitle = nameTitle;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAutoDoneText() {
        return autoDoneText;
    }

    public void setAutoDoneText(String autoDoneText) {
        this.autoDoneText = autoDoneText;
    }

    public String getAutoDoneBackgroundColor() {
        return autoDoneBackgroundColor;
    }

    public void setAutoDoneBackgroundColor(String autoDoneBackgroundColor) {
        this.autoDoneBackgroundColor = autoDoneBackgroundColor;
    }

    public String getAutoDoneTextColor() {
        return autoDoneTextColor;
    }

    public void setAutoDoneTextColor(String autoDoneTextColor) {
        this.autoDoneTextColor = autoDoneTextColor;
    }

    public String getLastReplyText() {
        return lastReplyText;
    }

    public void setLastReplyText(String lastReplyText) {
        this.lastReplyText = lastReplyText;
    }

    public String getFreeReturnText() {
        return freeReturnText;
    }

    public void setFreeReturnText(String freeReturnText) {
        this.freeReturnText = freeReturnText;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getExtraImageCountText() {
        return extraImageCountText;
    }

    public void setExtraImageCountText(String extraImageCountText) {
        this.extraImageCountText = extraImageCountText;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public int type(ResoInboxTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.resId);
        dest.writeInt(this.actionBy);
        dest.writeString(this.inboxMessage);
        dest.writeString(this.inboxMessageBackgroundColor);
        dest.writeString(this.inboxMessageTextColor);
        dest.writeString(this.invoiceNumber);
        dest.writeByte(this.isNotificationShow ? (byte) 1 : (byte) 0);
        dest.writeString(this.nameTitle);
        dest.writeString(this.userName);
        dest.writeString(this.autoDoneText);
        dest.writeString(this.autoDoneBackgroundColor);
        dest.writeString(this.autoDoneTextColor);
        dest.writeString(this.lastReplyText);
        dest.writeString(this.freeReturnText);
        dest.writeStringList(this.imageList);
        dest.writeString(this.extraImageCountText);
        dest.writeString(this.customerName);
        dest.writeString(this.sellerName);
        dest.writeByte(this.isLoadingItem ? (byte) 1 : (byte) 0);
    }

    protected InboxItemViewModel(Parcel in) {
        this.id = in.readInt();
        this.resId = in.readInt();
        this.actionBy = in.readInt();
        this.inboxMessage = in.readString();
        this.inboxMessageBackgroundColor = in.readString();
        this.inboxMessageTextColor = in.readString();
        this.invoiceNumber = in.readString();
        this.isNotificationShow = in.readByte() != 0;
        this.nameTitle = in.readString();
        this.userName = in.readString();
        this.autoDoneText = in.readString();
        this.autoDoneBackgroundColor = in.readString();
        this.autoDoneTextColor = in.readString();
        this.lastReplyText = in.readString();
        this.freeReturnText = in.readString();
        this.imageList = in.createStringArrayList();
        this.extraImageCountText = in.readString();
        this.customerName = in.readString();
        this.sellerName = in.readString();
        this.isLoadingItem = in.readByte() != 0;
    }

    public static final Creator<InboxItemViewModel> CREATOR = new Creator<InboxItemViewModel>() {
        @Override
        public InboxItemViewModel createFromParcel(Parcel source) {
            return new InboxItemViewModel(source);
        }

        @Override
        public InboxItemViewModel[] newArray(int size) {
            return new InboxItemViewModel[size];
        }
    };
}
