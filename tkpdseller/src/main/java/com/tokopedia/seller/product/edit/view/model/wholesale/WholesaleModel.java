package com.tokopedia.seller.product.edit.view.model.wholesale;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.base.view.adapter.ItemType;

/**
 * @author normansyahputa on 4/20/17.
 */
public class WholesaleModel implements Parcelable, ItemType {
    public static final int TYPE = 1212834;
    public static final Parcelable.Creator<WholesaleModel> CREATOR = new Parcelable.Creator<WholesaleModel>() {
        @Override
        public WholesaleModel createFromParcel(Parcel source) {
            return new WholesaleModel(source);
        }

        @Override
        public WholesaleModel[] newArray(int size) {
            return new WholesaleModel[size];
        }
    };
    /**
     * Main value : qty one, two, and price
     */
    private int qtyOne = 0;
    private int qtyTwo = 0;
    private double qtyPrice = 0;
    private int level;

    public WholesaleModel() {

    }

    public WholesaleModel(int quantityOne, int quantityTwo, double wholeSalePrice) {
        this.qtyOne = quantityOne;
        this.qtyTwo = quantityTwo;
        this.qtyPrice = wholeSalePrice;
    }

    protected WholesaleModel(Parcel in) {
        this.qtyOne = in.readInt();
        this.qtyTwo = in.readInt();
        this.qtyPrice = in.readDouble();
        this.level = in.readInt();
    }

    public static WholesaleModel invalidWholeSaleModel() {
        WholesaleModel wholesaleModel = new WholesaleModel(-1, -1, -1);
        return wholesaleModel;
    }

    public int getQtyOne() {
        return qtyOne;
    }

    public void setQtyOne(int qtyOne) {
        this.qtyOne = qtyOne;
    }

    public int getQtyTwo() {
        return qtyTwo;
    }

    public void setQtyTwo(int qtyTwo) {
        this.qtyTwo = qtyTwo;
    }

    public double getQtyPrice() {
        return qtyPrice;
    }

    public void setQtyPrice(double qtyPrice) {
        this.qtyPrice = qtyPrice;
    }

    @Override
    public String toString() {
        return " qty one : " + qtyOne + " qty two : " + qtyTwo + " qty price : " + qtyPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.qtyOne);
        dest.writeInt(this.qtyTwo);
        dest.writeDouble(this.qtyPrice);
        dest.writeInt(this.level);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WholesaleModel that = (WholesaleModel) o;

        if (qtyOne != that.qtyOne) return false;
        if (qtyTwo != that.qtyTwo) return false;
        return Double.compare(that.qtyPrice, qtyPrice) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = qtyOne;
        result = 31 * result + qtyTwo;
        temp = Double.doubleToLongBits(qtyPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
