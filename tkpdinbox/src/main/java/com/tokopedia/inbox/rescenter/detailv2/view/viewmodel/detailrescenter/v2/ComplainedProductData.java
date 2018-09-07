package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 08/11/17.
 */
public class ComplainedProductData implements Parcelable {

    public static final Parcelable.Creator<ComplainedProductData> CREATOR = new Parcelable.Creator<ComplainedProductData>() {
        @Override
        public ComplainedProductData createFromParcel(Parcel source) {
            return new ComplainedProductData(source);
        }

        @Override
        public ComplainedProductData[] newArray(int size) {
            return new ComplainedProductData[size];
        }
    };
    private int id;
    private int count;
    private ProductData product;
    private TroubleData trouble;

    public ComplainedProductData(int id, int count, ProductData product, TroubleData trouble) {
        this.id = id;
        this.count = count;
        this.product = product;
        this.trouble = trouble;
    }

    protected ComplainedProductData(Parcel in) {
        this.id = in.readInt();
        this.count = in.readInt();
        this.product = in.readParcelable(ProductData.class.getClassLoader());
        this.trouble = in.readParcelable(TroubleData.class.getClassLoader());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ProductData getProduct() {
        return product;
    }

    public void setProduct(ProductData product) {
        this.product = product;
    }

    public TroubleData getTrouble() {
        return trouble;
    }

    public void setTrouble(TroubleData trouble) {
        this.trouble = trouble;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.count);
        dest.writeParcelable(this.product, flags);
        dest.writeParcelable(this.trouble, flags);
    }
}
