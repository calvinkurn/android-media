package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationDomain implements Parcelable {

    public static final Parcelable.Creator<ConversationDomain> CREATOR = new Parcelable.Creator<ConversationDomain>() {
        @Override
        public ConversationDomain createFromParcel(Parcel source) {
            return new ConversationDomain(source);
        }

        @Override
        public ConversationDomain[] newArray(int size) {
            return new ConversationDomain[size];
        }
    };
    private int resConvId;
    private ConversationActionDomain action;
    private String message;
    private ConversationAddressDomain address;
    private ConversationShippingDetailDomain shippingDetail;
    private ConversationCreateTimeDomain createTime;
    private List<ConversationAttachmentDomain> attachment;
    private ConversationTroubleDomain trouble;
    private ConversationSolutionDomain solution;
    private List<ConversationProductDomain> product;
    private ConversationButtonDomain button;
    private ConversationFlagDomain flag;

    public ConversationDomain(int resConvId,
                              ConversationActionDomain action,
                              String message,
                              ConversationAddressDomain address,
                              ConversationShippingDetailDomain shippingDetail,
                              ConversationCreateTimeDomain createTime,
                              List<ConversationAttachmentDomain> attachment,
                              ConversationTroubleDomain trouble,
                              ConversationSolutionDomain solution,
                              List<ConversationProductDomain> product,
                              ConversationButtonDomain button,
                              ConversationFlagDomain flag) {
        this.resConvId = resConvId;
        this.action = action;
        this.message = message;
        this.address = address;
        this.shippingDetail = shippingDetail;
        this.createTime = createTime;
        this.attachment = attachment;
        this.trouble = trouble;
        this.solution = solution;
        this.product = product;
        this.button = button;
        this.flag = flag;
    }

    protected ConversationDomain(Parcel in) {
        this.resConvId = in.readInt();
        this.action = in.readParcelable(ConversationActionDomain.class.getClassLoader());
        this.message = in.readString();
        this.address = in.readParcelable(ConversationAddressDomain.class.getClassLoader());
        this.shippingDetail = in.readParcelable(ConversationShippingDetailDomain.class.getClassLoader());
        this.createTime = in.readParcelable(ConversationCreateTimeDomain.class.getClassLoader());
        this.attachment = in.createTypedArrayList(ConversationAttachmentDomain.CREATOR);
        this.trouble = in.readParcelable(ConversationTroubleDomain.class.getClassLoader());
        this.solution = in.readParcelable(ConversationSolutionDomain.class.getClassLoader());
        this.product = new ArrayList<ConversationProductDomain>();
        in.readList(this.product, ConversationProductDomain.class.getClassLoader());
        this.button = in.readParcelable(ConversationButtonDomain.class.getClassLoader());
        this.flag = in.readParcelable(ConversationFlagDomain.class.getClassLoader());
    }

    public int getResConvId() {
        return resConvId;
    }

    public void setResConvId(int resConvId) {
        this.resConvId = resConvId;
    }

    public ConversationActionDomain getAction() {
        return action;
    }

    public void setAction(ConversationActionDomain action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ConversationAddressDomain getAddress() {
        return address;
    }

    public void setAddress(ConversationAddressDomain address) {
        this.address = address;
    }

    public ConversationShippingDetailDomain getShippingDetail() {
        return shippingDetail;
    }

    public void setShippingDetail(ConversationShippingDetailDomain shippingDetail) {
        this.shippingDetail = shippingDetail;
    }

    public ConversationCreateTimeDomain getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ConversationCreateTimeDomain createTime) {
        this.createTime = createTime;
    }

    public List<ConversationAttachmentDomain> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<ConversationAttachmentDomain> attachment) {
        this.attachment = attachment;
    }

    public ConversationTroubleDomain getTrouble() {
        return trouble;
    }

    public void setTrouble(ConversationTroubleDomain trouble) {
        this.trouble = trouble;
    }

    public ConversationSolutionDomain getSolution() {
        return solution;
    }

    public void setSolution(ConversationSolutionDomain solution) {
        this.solution = solution;
    }

    public List<ConversationProductDomain> getProduct() {
        return product;
    }

    public void setProduct(List<ConversationProductDomain> product) {
        this.product = product;
    }

    public ConversationButtonDomain getButton() {
        return button;
    }

    public void setButton(ConversationButtonDomain button) {
        this.button = button;
    }

    public ConversationFlagDomain getFlag() {
        return flag;
    }

    public void setFlag(ConversationFlagDomain flag) {
        this.flag = flag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.resConvId);
        dest.writeParcelable(this.action, flags);
        dest.writeString(this.message);
        dest.writeParcelable(this.address, flags);
        dest.writeParcelable(this.shippingDetail, flags);
        dest.writeParcelable(this.createTime, flags);
        dest.writeTypedList(this.attachment);
        dest.writeParcelable(this.trouble, flags);
        dest.writeParcelable(this.solution, flags);
        dest.writeList(this.product);
        dest.writeParcelable(this.button, flags);
        dest.writeParcelable(this.flag, flags);
    }
}
