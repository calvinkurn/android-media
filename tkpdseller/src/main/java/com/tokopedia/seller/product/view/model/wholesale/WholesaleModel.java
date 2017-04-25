package com.tokopedia.seller.product.view.model.wholesale;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.topads.view.model.TypeBasedModel;

import java.lang.ref.WeakReference;

/**
 * @author normansyahputa on 4/20/17.
 *         based on {@link com.tokopedia.seller.myproduct.customview.wholesale,WholesaleModel}
 */
public class WholesaleModel extends TypeBasedModel implements Parcelable {
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
    private WeakReference<WholesaleModel> prevWholesaleModel;
    private WeakReference<WholesaleModel> nextWholesaleModel;

    public WholesaleModel() {
        super(TYPE);
    }

    public WholesaleModel(int quantityOne, int quantityTwo, double wholeSalePrice) {
        super(TYPE);
        this.qtyOne = quantityOne;
        this.qtyTwo = quantityTwo;
        this.qtyPrice = wholeSalePrice;
    }

    protected WholesaleModel(Parcel in) {
        super(TYPE);
        this.qtyOne = in.readInt();
        this.qtyTwo = in.readInt();
        this.qtyPrice = in.readDouble();
        this.level = in.readInt();

        WholesaleModel prevwholesaleModel = in.readParcelable(WholesaleModel.class.getClassLoader());
        if (prevwholesaleModel != null)
            this.prevWholesaleModel = new WeakReference<WholesaleModel>(prevwholesaleModel);

        WholesaleModel nextWholesaleModel = in.readParcelable(WholesaleModel.class.getClassLoader());
        if (nextWholesaleModel != null)
            this.nextWholesaleModel = new WeakReference<WholesaleModel>(nextWholesaleModel);
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

    public WholesaleModel getPrevWholesaleModel() {
        return prevWholesaleModel != null ? prevWholesaleModel.get() : null;
    }

    public void setPrevWholesaleModel(WholesaleModel wholesaleModel) {
        prevWholesaleModel = new WeakReference<WholesaleModel>(wholesaleModel);
    }

    public void setNextWholesaleModel(WholesaleModel wholesaleModel) {
        nextWholesaleModel = new WeakReference<WholesaleModel>(wholesaleModel);
    }

    public WholesaleModel getNexWholesaleModel() {
        return nextWholesaleModel != null ? nextWholesaleModel.get() : null;
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
        if (prevWholesaleModel != null && prevWholesaleModel.get() != null)
            dest.writeParcelable(this.prevWholesaleModel.get(), flags);
        if (nextWholesaleModel != null && nextWholesaleModel.get() != null)
            dest.writeParcelable(this.nextWholesaleModel.get(), flags);
    }
}
