package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 08/11/17.
 */
public class ProductData implements Parcelable {

    public static final Parcelable.Creator<ProductData> CREATOR = new Parcelable.Creator<ProductData>() {
        @Override
        public ProductData createFromParcel(Parcel source) {
            return new ProductData(source);
        }

        @Override
        public ProductData[] newArray(int size) {
            return new ProductData[size];
        }
    };
    private String name;
    private String thumb;
    private String variant;
    private AmountData amount;
    private int quantity;

    public ProductData(String name, String thumb, String variant, AmountData amount, int quantity) {
        this.name = name;
        this.thumb = thumb;
        this.variant = variant;
        this.amount = amount;
        this.quantity = quantity;
    }

    protected ProductData(Parcel in) {
        this.name = in.readString();
        this.thumb = in.readString();
        this.variant = in.readString();
        this.amount = in.readParcelable(AmountData.class.getClassLoader());
        this.quantity = in.readInt();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public AmountData getAmount() {
        return amount;
    }

    public void setAmount(AmountData amount) {
        this.amount = amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.thumb);
        dest.writeString(this.variant);
        dest.writeParcelable(this.amount, flags);
        dest.writeInt(this.quantity);
    }
}
