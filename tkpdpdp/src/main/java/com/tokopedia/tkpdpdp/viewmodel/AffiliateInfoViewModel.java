package com.tokopedia.tkpdpdp.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class AffiliateInfoViewModel implements Parcelable {

    private int adId;
    private int commissionPercent;
    private String commissionPercentDispay;
    private int commissionValue;
    private String commissionValueDisplay;
    private int productId;
    private String uniqueURL;

    public AffiliateInfoViewModel(int adId,
                                  int commissionPercent,
                                  String commissionPercentDispay,
                                  int commissionValue,
                                  String commissionValueDisplay,
                                  int productId,
                                  String uniqueURL) {
        this.adId = adId;
        this.commissionPercent = commissionPercent;
        this.commissionPercentDispay = commissionPercentDispay;
        this.commissionValue = commissionValue;
        this.commissionValueDisplay = commissionValueDisplay;
        this.productId = productId;
        this.uniqueURL = uniqueURL;
    }

    public AffiliateInfoViewModel() {
    }

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public int getCommissionPercent() {
        return commissionPercent;
    }

    public void setCommissionPercent(int commissionPercent) {
        this.commissionPercent = commissionPercent;
    }

    public String getCommissionPercentDispay() {
        return commissionPercentDispay;
    }

    public void setCommissionPercentDispay(String commissionPercentDispay) {
        this.commissionPercentDispay = commissionPercentDispay;
    }

    public int getCommissionValue() {
        return commissionValue;
    }

    public void setCommissionValue(int commissionValue) {
        this.commissionValue = commissionValue;
    }

    public String getCommissionValueDisplay() {
        return commissionValueDisplay;
    }

    public void setCommissionValueDisplay(String commissionValueDisplay) {
        this.commissionValueDisplay = commissionValueDisplay;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getUniqueURL() {
        return uniqueURL;
    }

    public void setUniqueURL(String uniqueURL) {
        this.uniqueURL = uniqueURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.adId);
        dest.writeInt(this.commissionPercent);
        dest.writeString(this.commissionPercentDispay);
        dest.writeInt(this.commissionValue);
        dest.writeString(this.commissionValueDisplay);
        dest.writeInt(this.productId);
        dest.writeString(this.uniqueURL);
    }

    protected AffiliateInfoViewModel(Parcel in) {
        this.adId = in.readInt();
        this.commissionPercent = in.readInt();
        this.commissionPercentDispay = in.readString();
        this.commissionValue = in.readInt();
        this.commissionValueDisplay = in.readString();
        this.productId = in.readInt();
        this.uniqueURL = in.readString();
    }

    public static final Parcelable.Creator<AffiliateInfoViewModel> CREATOR = new Parcelable.Creator<AffiliateInfoViewModel>() {
        @Override
        public AffiliateInfoViewModel createFromParcel(Parcel source) {
            return new AffiliateInfoViewModel(source);
        }

        @Override
        public AffiliateInfoViewModel[] newArray(int size) {
            return new AffiliateInfoViewModel[size];
        }
    };
}
