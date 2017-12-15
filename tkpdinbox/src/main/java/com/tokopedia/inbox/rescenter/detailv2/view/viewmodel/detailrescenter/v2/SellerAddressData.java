package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 07/11/17.
 */
public class SellerAddressData implements Parcelable {

    public static final Parcelable.Creator<SellerAddressData> CREATOR = new Parcelable.Creator<SellerAddressData>() {
        @Override
        public SellerAddressData createFromParcel(Parcel source) {
            return new SellerAddressData(source);
        }

        @Override
        public SellerAddressData[] newArray(int size) {
            return new SellerAddressData[size];
        }
    };
    private AddressData address;
    private ByData by;
    private String createTime;
    private String createTimeStr;
    private int conversationId;

    public SellerAddressData(AddressData address, ByData by, String createTime, String createTimeStr, int conversationId) {
        this.address = address;
        this.by = by;
        this.createTime = createTime;
        this.createTimeStr = createTimeStr;
        this.conversationId = conversationId;
    }

    protected SellerAddressData(Parcel in) {
        this.address = in.readParcelable(AddressData.class.getClassLoader());
        this.by = in.readParcelable(ByData.class.getClassLoader());
        this.createTime = in.readString();
        this.createTimeStr = in.readString();
        this.conversationId = in.readInt();
    }

    public AddressData getAddress() {
        return address;
    }

    public void setAddress(AddressData address) {
        this.address = address;
    }

    public ByData getBy() {
        return by;
    }

    public void setBy(ByData by) {
        this.by = by;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.address, flags);
        dest.writeParcelable(this.by, flags);
        dest.writeString(this.createTime);
        dest.writeString(this.createTimeStr);
        dest.writeInt(this.conversationId);
    }
}
