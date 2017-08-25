package com.tokopedia.seller.product.variant.data.model.variantsubmit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * metrics between  the variant of level 1 + level 2 + so on.
 * Created by hendry on 8/15/2017.
 */

public class ProductVariantCombinationSubmit implements Parcelable {

    @SerializedName("st")
    @Expose
    private int status;
    @SerializedName("opt")
    @Expose
    private List<Long> optionList;
    @SerializedName("pvd")
    @Expose
    private int pvd;

    /**
     * get status of this metrics
     *
     * @return 0 if not available. 1 if available.
     */
    public int getStatus() {
        return status;
    }

    /**
     * status of the variant. 0: not available. 1: available
     *
     * @param status set 0 for not available (or zero stock)
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * get the list of combination between t_id of variant level 1 + variant level 2 + so on
     *
     * @return optionList
     */
    public List<Long> getOptionList() {
        return optionList;
    }

    /**
     * set this list with combination between t_id of variant level 1 + variant level 2 + so on
     *
     * @param optionList
     */
    public void setOptionList(List<Long> optionList) {
        this.optionList = optionList;
    }

    /**
     * product variant data id (got from the server)
     *
     * @return "0" if this metric is just new created. "1" if this metric is existing from server
     */
    public int getPvd() {
        return pvd;
    }

    /**
     * set with the pvd got from the server
     * set with 0 if this is new created.
     *
     * @param pvd "0" if new created. pvd id if this is got from server.
     */
    public void setPvd(int pvd) {
        this.pvd = pvd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeList(this.optionList);
        dest.writeInt(this.pvd);
    }

    public ProductVariantCombinationSubmit() {
    }

    protected ProductVariantCombinationSubmit(Parcel in) {
        this.status = in.readInt();
        this.optionList = new ArrayList<Long>();
        in.readList(this.optionList, Long.class.getClassLoader());
        this.pvd = in.readInt();
    }

    public static final Creator<ProductVariantCombinationSubmit> CREATOR = new Creator<ProductVariantCombinationSubmit>() {
        @Override
        public ProductVariantCombinationSubmit createFromParcel(Parcel source) {
            return new ProductVariantCombinationSubmit(source);
        }

        @Override
        public ProductVariantCombinationSubmit[] newArray(int size) {
            return new ProductVariantCombinationSubmit[size];
        }
    };
}
