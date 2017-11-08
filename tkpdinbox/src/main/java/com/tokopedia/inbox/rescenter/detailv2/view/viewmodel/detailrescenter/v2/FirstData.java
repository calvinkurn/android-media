package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 07/11/17.
 */
public class FirstData implements Parcelable {

    private String buyerRemark;

    public FirstData(String buyerRemark) {
        this.buyerRemark = buyerRemark;
    }

    public String getBuyerRemark() {
        return buyerRemark;
    }

    public void setBuyerRemark(String buyerRemark) {
        this.buyerRemark = buyerRemark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.buyerRemark);
    }

    protected FirstData(Parcel in) {
        this.buyerRemark = in.readString();
    }

    public static final Parcelable.Creator<FirstData> CREATOR = new Parcelable.Creator<FirstData>() {
        @Override
        public FirstData createFromParcel(Parcel source) {
            return new FirstData(source);
        }

        @Override
        public FirstData[] newArray(int size) {
            return new FirstData[size];
        }
    };
}
