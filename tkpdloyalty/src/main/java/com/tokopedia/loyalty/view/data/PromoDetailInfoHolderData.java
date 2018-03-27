package com.tokopedia.loyalty.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Aghny A. Putra on 26/03/18
 */

public class PromoDetailInfoHolderData implements Parcelable {

    private String thumbnailImageUrl;
    private String title;
    private String promoPeriod;
    private String minTransaction;

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    public void setThumbnailImageUrl(String thumbnailImageUrl) {
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPromoPeriod() {
        return promoPeriod;
    }

    public void setPromoPeriod(String promoPeriod) {
        this.promoPeriod = promoPeriod;
    }

    public String getMinTransaction() {
        return minTransaction;
    }

    public void setMinTransaction(String minTransaction) {
        this.minTransaction = minTransaction;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.thumbnailImageUrl);
        dest.writeString(this.title);
        dest.writeString(this.promoPeriod);
        dest.writeString(this.minTransaction);
    }

    public PromoDetailInfoHolderData() {
    }

    protected PromoDetailInfoHolderData(Parcel in) {
        this.thumbnailImageUrl = in.readString();
        this.title = in.readString();
        this.promoPeriod = in.readString();
        this.minTransaction = in.readString();
    }

    public static final Creator<PromoDetailInfoHolderData> CREATOR = new Creator<PromoDetailInfoHolderData>() {
        @Override
        public PromoDetailInfoHolderData createFromParcel(Parcel source) {
            return new PromoDetailInfoHolderData(source);
        }

        @Override
        public PromoDetailInfoHolderData[] newArray(int size) {
            return new PromoDetailInfoHolderData[size];
        }
    };
}