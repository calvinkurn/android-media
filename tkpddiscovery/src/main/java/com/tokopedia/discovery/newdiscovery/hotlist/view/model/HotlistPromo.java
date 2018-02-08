package com.tokopedia.discovery.newdiscovery.hotlist.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nakama on 12/5/17.
 */

public class HotlistPromo implements Parcelable {

    private String title;
    private String voucherCode;
    private String minimunTransaction;
    private String promoPeriod;
    private String applinkTermCondition;
    private String urlTermCondition;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getMinimunTransaction() {
        return minimunTransaction;
    }

    public void setMinimunTransaction(String minimunTransaction) {
        this.minimunTransaction = minimunTransaction;
    }

    public String getPromoPeriod() {
        return promoPeriod;
    }

    public void setPromoPeriod(String promoPeriod) {
        this.promoPeriod = promoPeriod;
    }

    public String getApplinkTermCondition() {
        return applinkTermCondition;
    }

    public void setApplinkTermCondition(String applinkTermCondition) {
        this.applinkTermCondition = applinkTermCondition;
    }


    public HotlistPromo() {
    }

    public void setUrlTermCondition(String urlTermCondition) {
        this.urlTermCondition = urlTermCondition;
    }

    public String getUrlTermCondition() {
        return urlTermCondition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.voucherCode);
        dest.writeString(this.minimunTransaction);
        dest.writeString(this.promoPeriod);
        dest.writeString(this.applinkTermCondition);
        dest.writeString(this.urlTermCondition);
    }

    protected HotlistPromo(Parcel in) {
        this.title = in.readString();
        this.voucherCode = in.readString();
        this.minimunTransaction = in.readString();
        this.promoPeriod = in.readString();
        this.applinkTermCondition = in.readString();
        this.urlTermCondition = in.readString();
    }

    public static final Creator<HotlistPromo> CREATOR = new Creator<HotlistPromo>() {
        @Override
        public HotlistPromo createFromParcel(Parcel source) {
            return new HotlistPromo(source);
        }

        @Override
        public HotlistPromo[] newArray(int size) {
            return new HotlistPromo[size];
        }
    };
}
