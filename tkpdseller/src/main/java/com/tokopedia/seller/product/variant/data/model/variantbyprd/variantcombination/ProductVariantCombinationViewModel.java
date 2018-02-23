
package com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductVariantCombinationViewModel implements Parcelable{

    public static final int ACTIVE_STATUS = 1; // from API

    @SerializedName("st")
    @Expose
    private int st;

    @SerializedName("price_var")
    @Expose
    private long priceVar;

    @SerializedName("stock")
    @Expose
    private long stock;

    @SerializedName("sku")
    @Expose
    private String sku;

    @SerializedName("opt")
    @Expose
    private List<Integer> opt = null; // option combination of t_id of selected variants

    public boolean isActive(){
        return st == ACTIVE_STATUS;
    }

    public boolean hasStock(){
        return st == ACTIVE_STATUS || stock > 0;
    }

    public int getSt() {
        return st;
    }

    public void setSt(int st) {
        this.st = st;
    }

    public List<Integer> getOpt() {
        return opt;
    }

    public void setOpt(List<Integer> opt) {
        this.opt = opt;
    }

    public long getPriceVar() {
        return priceVar;
    }

    public void setPriceVar(long priceVar) {
        this.priceVar = priceVar;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.st);
        dest.writeLong(this.priceVar);
        dest.writeLong(this.stock);
        dest.writeString(this.sku);
        dest.writeList(this.opt);
    }

    public ProductVariantCombinationViewModel() {
    }

    protected ProductVariantCombinationViewModel(Parcel in) {
        this.st = in.readInt();
        this.priceVar = in.readLong();
        this.stock = in.readLong();
        this.sku = in.readString();
        this.opt = new ArrayList<Integer>();
        in.readList(this.opt, Integer.class.getClassLoader());
    }

    public static final Creator<ProductVariantCombinationViewModel> CREATOR = new Creator<ProductVariantCombinationViewModel>() {
        @Override
        public ProductVariantCombinationViewModel createFromParcel(Parcel source) {
            return new ProductVariantCombinationViewModel(source);
        }

        @Override
        public ProductVariantCombinationViewModel[] newArray(int size) {
            return new ProductVariantCombinationViewModel[size];
        }
    };
}
