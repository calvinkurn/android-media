package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 07/11/17.
 */
public class InvoiceData implements Parcelable {
    public static final Parcelable.Creator<InvoiceData> CREATOR = new Parcelable.Creator<InvoiceData>() {
        @Override
        public InvoiceData createFromParcel(Parcel source) {
            return new InvoiceData(source);
        }

        @Override
        public InvoiceData[] newArray(int size) {
            return new InvoiceData[size];
        }
    };
    private String refNum;
    private String url;

    public InvoiceData(String refNum, String url) {
        this.refNum = refNum;
        this.url = url;
    }

    protected InvoiceData(Parcel in) {
        this.refNum = in.readString();
        this.url = in.readString();
    }

    public String getRefNum() {
        return refNum;
    }

    public void setRefNum(String refNum) {
        this.refNum = refNum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.refNum);
        dest.writeString(this.url);
    }
}
