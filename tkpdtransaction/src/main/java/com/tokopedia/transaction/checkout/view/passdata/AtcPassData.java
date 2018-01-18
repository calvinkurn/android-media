package com.tokopedia.transaction.checkout.view.passdata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class AtcPassData implements Parcelable {
    public static final int CURRENCY_IDR = 1;
    public static final int CURRENCY_USD = 2;

    public static final int WEIGHT_KILOS = 1;
    public static final int WEIGHT_GRAM = 2;

    private String productId;
    private String productName;
    private int minimalQtyOrder;
    private double pricePlan;
    private int priceCurrency;
    private String priceFormatted;
    private String productImage;
    private String productVarianRemark;
    private double weightPlan;
    private int weightUnit;
    private String weightFormatted;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getMinimalQtyOrder() {
        return minimalQtyOrder;
    }

    public void setMinimalQtyOrder(int minimalQtyOrder) {
        this.minimalQtyOrder = minimalQtyOrder;
    }

    public double getPricePlan() {
        return pricePlan;
    }

    public void setPricePlan(double pricePlan) {
        this.pricePlan = pricePlan;
    }

    public int getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(int priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public String getPriceFormatted() {
        return priceFormatted;
    }

    public void setPriceFormatted(String priceFormatted) {
        this.priceFormatted = priceFormatted;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductVarianRemark() {
        return productVarianRemark;
    }

    public void setProductVarianRemark(String productVarianRemark) {
        this.productVarianRemark = productVarianRemark;
    }

    public double getWeightPlan() {
        return weightPlan;
    }

    public void setWeightPlan(double weightPlan) {
        this.weightPlan = weightPlan;
    }

    public int getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(int weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getWeightFormatted() {
        return weightFormatted;
    }

    public void setWeightFormatted(String weightFormatted) {
        this.weightFormatted = weightFormatted;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productId);
        dest.writeString(this.productName);
        dest.writeInt(this.minimalQtyOrder);
        dest.writeDouble(this.pricePlan);
        dest.writeInt(this.priceCurrency);
        dest.writeString(this.priceFormatted);
        dest.writeString(this.productImage);
        dest.writeString(this.productVarianRemark);
        dest.writeDouble(this.weightPlan);
        dest.writeInt(this.weightUnit);
        dest.writeString(this.weightFormatted);
    }

    public AtcPassData() {
    }

    protected AtcPassData(Parcel in) {
        this.productId = in.readString();
        this.productName = in.readString();
        this.minimalQtyOrder = in.readInt();
        this.pricePlan = in.readDouble();
        this.priceCurrency = in.readInt();
        this.priceFormatted = in.readString();
        this.productImage = in.readString();
        this.productVarianRemark = in.readString();
        this.weightPlan = in.readDouble();
        this.weightUnit = in.readInt();
        this.weightFormatted = in.readString();
    }

    public static final Parcelable.Creator<AtcPassData> CREATOR = new Parcelable.Creator<AtcPassData>() {
        @Override
        public AtcPassData createFromParcel(Parcel source) {
            return new AtcPassData(source);
        }

        @Override
        public AtcPassData[] newArray(int size) {
            return new AtcPassData[size];
        }
    };
}
