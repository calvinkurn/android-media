package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 10/10/17.
 */

public class DetailResChatDomain implements Parcelable {

    public static final Creator<DetailResChatDomain> CREATOR = new Creator<DetailResChatDomain>() {
        @Override
        public DetailResChatDomain createFromParcel(Parcel source) {
            return new DetailResChatDomain(source);
        }

        @Override
        public DetailResChatDomain[] newArray(int size) {
            return new DetailResChatDomain[size];
        }
    };
    private NextActionDomain nextAction;
    private ShopDomain shop;
    private CustomerDomain customer;
    private ResolutionDomain resolution;
    private ButtonDomain button;
    private int actionBy;
    private ConversationListDomain conversationList;
    private OrderDomain order;
    private LastDomain last;
    private boolean success;

    public DetailResChatDomain(NextActionDomain nextAction,
                               ShopDomain shop,
                               CustomerDomain customer,
                               ResolutionDomain resolution,
                               ButtonDomain button,
                               int actionBy,
                               ConversationListDomain conversationList,
                               OrderDomain order,
                               LastDomain last) {
        this.nextAction = nextAction;
        this.shop = shop;
        this.customer = customer;
        this.resolution = resolution;
        this.button = button;
        this.actionBy = actionBy;
        this.conversationList = conversationList;
        this.order = order;
        this.last = last;
    }

    protected DetailResChatDomain(Parcel in) {
        this.nextAction = in.readParcelable(NextActionDomain.class.getClassLoader());
        this.shop = in.readParcelable(ShopDomain.class.getClassLoader());
        this.customer = in.readParcelable(CustomerDomain.class.getClassLoader());
        this.resolution = in.readParcelable(ResolutionDomain.class.getClassLoader());
        this.button = in.readParcelable(ButtonDomain.class.getClassLoader());
        this.actionBy = in.readInt();
        this.conversationList = in.readParcelable(ConversationListDomain.class.getClassLoader());
        this.order = in.readParcelable(OrderDomain.class.getClassLoader());
        this.last = in.readParcelable(LastDomain.class.getClassLoader());
        this.success = in.readByte() != 0;
    }

    public NextActionDomain getNextAction() {
        return nextAction;
    }

    public void setNextAction(NextActionDomain nextAction) {
        this.nextAction = nextAction;
    }

    public ShopDomain getShop() {
        return shop;
    }

    public void setShop(ShopDomain shop) {
        this.shop = shop;
    }

    public CustomerDomain getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDomain customer) {
        this.customer = customer;
    }

    public ResolutionDomain getResolution() {
        return resolution;
    }

    public void setResolution(ResolutionDomain resolution) {
        this.resolution = resolution;
    }

    public ButtonDomain getButton() {
        return button;
    }

    public void setButton(ButtonDomain button) {
        this.button = button;
    }

    public int getActionBy() {
        return actionBy;
    }

    public void setActionBy(int actionBy) {
        this.actionBy = actionBy;
    }

    public ConversationListDomain getConversationList() {
        return conversationList;
    }

    public void setConversationList(ConversationListDomain conversationList) {
        this.conversationList = conversationList;
    }

    public OrderDomain getOrder() {
        return order;
    }

    public void setOrder(OrderDomain order) {
        this.order = order;
    }

    public LastDomain getLast() {
        return last;
    }

    public void setLast(LastDomain last) {
        this.last = last;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.nextAction, flags);
        dest.writeParcelable(this.shop, flags);
        dest.writeParcelable(this.customer, flags);
        dest.writeParcelable(this.resolution, flags);
        dest.writeParcelable(this.button, flags);
        dest.writeInt(this.actionBy);
        dest.writeParcelable(this.conversationList, flags);
        dest.writeParcelable(this.order, flags);
        dest.writeParcelable(this.last, flags);
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
    }
}