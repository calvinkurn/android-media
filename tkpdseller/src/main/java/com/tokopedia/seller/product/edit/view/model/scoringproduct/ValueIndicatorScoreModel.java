package com.tokopedia.seller.product.edit.view.model.scoringproduct;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zulfikarrahman on 4/17/17.
 */

public class ValueIndicatorScoreModel implements Parcelable {
    private int imageCount;
    private int imageResolution;
    private int lengthProductName;
    private int lengthDescProduct;
    private boolean stockStatus;
    private boolean freeReturnStatus;
    private boolean freeReturnActive;
    private boolean isVariantActive;
    private boolean hasVideo;
    private boolean hasCatalog;

    public ValueIndicatorScoreModel(int imageCount, int imageResolution,
                                    int lengthProductName, int lengthDescProduct, boolean stockStatus,
                                    boolean cashbackStatus, boolean freeReturnStatus, boolean videoStatus,
                                    boolean isVariantActive, boolean hasVideo, boolean hasCatalog) {
        this.imageCount = imageCount;
        this.imageResolution = imageResolution;
        this.lengthProductName = lengthProductName;
        this.lengthDescProduct = lengthDescProduct;
        this.stockStatus = stockStatus;
        this.freeReturnStatus = freeReturnStatus;
        this.isVariantActive = isVariantActive;
        this.hasVideo = hasVideo;
        this.hasCatalog = hasCatalog;
    }

    public void setHasCatalog(boolean hasCatalog) {
        this.hasCatalog = hasCatalog;
    }

    public boolean isHasCatalog() {
        return hasCatalog;
    }

    public boolean isVariantActive() {
        return isVariantActive;
    }

    public void setVariantActive(boolean variantActive) {
        isVariantActive = variantActive;
    }

    public boolean isHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public ValueIndicatorScoreModel() {
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public int getImageResolution() {
        return imageResolution;
    }

    public void setImageResolution(int imageResolution) {
        this.imageResolution = imageResolution;
    }

    public int getLengthProductName() {
        return lengthProductName;
    }

    public void setLengthProductName(int lengthProductName) {
        this.lengthProductName = lengthProductName;
    }

    public int getLengthDescProduct() {
        return lengthDescProduct;
    }

    public void setLengthDescProduct(int lengthDescProduct) {
        this.lengthDescProduct = lengthDescProduct;
    }

    public boolean isStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(boolean stockStatus) {
        this.stockStatus = stockStatus;
    }

    public boolean isFreeReturnStatus() {
        return freeReturnStatus;
    }

    public void setFreeReturnStatus(boolean freeReturnStatus) {
        this.freeReturnStatus = freeReturnStatus;
    }

    public boolean isFreeReturnActive() {
        return freeReturnActive;
    }

    public void setFreeReturnActive(boolean freeReturnActive) {
        this.freeReturnActive = freeReturnActive;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.imageCount);
        dest.writeInt(this.imageResolution);
        dest.writeInt(this.lengthProductName);
        dest.writeInt(this.lengthDescProduct);
        dest.writeByte(this.stockStatus ? (byte) 1 : (byte) 0);
        dest.writeByte(this.freeReturnStatus ? (byte) 1 : (byte) 0);
        dest.writeByte(this.freeReturnActive ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isVariantActive ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasVideo ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasCatalog ? (byte) 1 : (byte) 0);
    }

    protected ValueIndicatorScoreModel(Parcel in) {
        this.imageCount = in.readInt();
        this.imageResolution = in.readInt();
        this.lengthProductName = in.readInt();
        this.lengthDescProduct = in.readInt();
        this.stockStatus = in.readByte() != 0;
        this.freeReturnStatus = in.readByte() != 0;
        this.freeReturnActive = in.readByte() != 0;
        this.isVariantActive = in.readByte() != 0;
        this.hasVideo = in.readByte() != 0;
        this.hasCatalog = in.readByte() != 0;
    }

    public static final Creator<ValueIndicatorScoreModel> CREATOR = new Creator<ValueIndicatorScoreModel>() {
        @Override
        public ValueIndicatorScoreModel createFromParcel(Parcel source) {
            return new ValueIndicatorScoreModel(source);
        }

        @Override
        public ValueIndicatorScoreModel[] newArray(int size) {
            return new ValueIndicatorScoreModel[size];
        }
    };
}
