
package com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductVariantCombinationViewModel implements Parcelable{

    public static final int ACTIVE_STATUS = 1; // from API
    public static final int NOT_ACTIVE_STATUS = 0; // from API

    @SerializedName("st")
    @Expose
    private int st;

    @SerializedName("price_var")
    @Expose
    private double priceVar;

    @SerializedName("stock")
    @Expose
    private long stock;

    @SerializedName("sku")
    @Expose
    private String sku;

    @SerializedName("opt")
    @Expose
    private List<Integer> opt = null; // option combination of t_id of selected variants

    private String level1String;
    private String level2String;

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

    public void setActive(boolean isActive) {
        this.st = isActive? ACTIVE_STATUS: NOT_ACTIVE_STATUS;
    }

    public List<Integer> getOpt() {
        return opt;
    }

    public void setOpt(List<Integer> opt) {
        this.opt = opt;
    }

    public double getPriceVar() {
        return priceVar;
    }

    public void setPriceVar(double priceVar) {
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

    public String getLevel1String() {
        return level1String == null ? "" : level1String;
    }

    public void setLevel1String(String level1String) {
        this.level1String = level1String;
    }

    public String getLevel2String() {
        return level2String == null ? "" : level2String;
    }

    public String getLeafString() {
        return TextUtils.isEmpty(level2String) ? level1String: level2String;
    }

    public void setLevel2String(String level2String) {
        this.level2String = level2String;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.st);
        dest.writeDouble(this.priceVar);
        dest.writeLong(this.stock);
        dest.writeString(this.sku);
        dest.writeList(this.opt);
        dest.writeString(this.level1String);
        dest.writeString(this.level2String);
    }

    public ProductVariantCombinationViewModel() {
    }

    protected ProductVariantCombinationViewModel(Parcel in) {
        this.st = in.readInt();
        this.priceVar = in.readDouble();
        this.stock = in.readLong();
        this.sku = in.readString();
        this.opt = new ArrayList<Integer>();
        in.readList(this.opt, Integer.class.getClassLoader());
        this.level1String = in.readString();
        this.level2String = in.readString();
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
