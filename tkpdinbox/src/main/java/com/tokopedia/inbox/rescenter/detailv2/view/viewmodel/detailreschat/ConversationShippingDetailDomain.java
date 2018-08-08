package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationShippingDetailDomain implements Parcelable {

    public static final Parcelable.Creator<ConversationShippingDetailDomain> CREATOR = new Parcelable.Creator<ConversationShippingDetailDomain>() {
        @Override
        public ConversationShippingDetailDomain createFromParcel(Parcel source) {
            return new ConversationShippingDetailDomain(source);
        }

        @Override
        public ConversationShippingDetailDomain[] newArray(int size) {
            return new ConversationShippingDetailDomain[size];
        }
    };
    private String awbNumber;
    private int id;
    private String name;

    public ConversationShippingDetailDomain(String awbNumber, int id, String name) {
        this.awbNumber = awbNumber;
        this.id = id;
        this.name = name;
    }

    protected ConversationShippingDetailDomain(Parcel in) {
        this.awbNumber = in.readString();
        this.id = in.readInt();
        this.name = in.readString();
    }

    public String getAwbNumber() {
        return awbNumber;
    }

    public void setAwbNumber(String awbNumber) {
        this.awbNumber = awbNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.awbNumber);
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }
}
