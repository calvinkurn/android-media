package com.tokopedia.loyalty.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 03/01/18.
 */

public class PromoData implements Parcelable {
    private String title;
    private String promoCode;
    private String appLink;
    private String promoLink;
    private String minTransaction;
    private String thumbnailImage;
    private String startDate;
    private String endDate;
    private boolean isMultiplePromo;
    private String periodFormatted;

    public String getPeriodFormatted() {
        return periodFormatted;
    }

    public void setPeriodFormatted(String periodFormatted) {
        this.periodFormatted = periodFormatted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public String getPromoLink() {
        return promoLink;
    }

    public void setPromoLink(String promoLink) {
        this.promoLink = promoLink;
    }

    public String getMinTransaction() {
        return minTransaction;
    }

    public void setMinTransaction(String minTransaction) {
        this.minTransaction = minTransaction;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isMultiplePromo() {
        return isMultiplePromo;
    }

    public void setMultiplePromo(boolean multiplePromo) {
        isMultiplePromo = multiplePromo;
    }

    public PromoData() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.promoCode);
        dest.writeString(this.appLink);
        dest.writeString(this.promoLink);
        dest.writeString(this.minTransaction);
        dest.writeString(this.thumbnailImage);
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeByte(this.isMultiplePromo ? (byte) 1 : (byte) 0);
        dest.writeString(this.periodFormatted);
    }

    protected PromoData(Parcel in) {
        this.title = in.readString();
        this.promoCode = in.readString();
        this.appLink = in.readString();
        this.promoLink = in.readString();
        this.minTransaction = in.readString();
        this.thumbnailImage = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.isMultiplePromo = in.readByte() != 0;
        this.periodFormatted = in.readString();
    }

    public static final Creator<PromoData> CREATOR = new Creator<PromoData>() {
        @Override
        public PromoData createFromParcel(Parcel source) {
            return new PromoData(source);
        }

        @Override
        public PromoData[] newArray(int size) {
            return new PromoData[size];
        }
    };
}
