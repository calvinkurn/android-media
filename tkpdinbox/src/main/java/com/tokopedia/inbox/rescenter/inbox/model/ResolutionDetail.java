
package com.tokopedia.inbox.rescenter.inbox.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResolutionDetail implements Parcelable {

    @SerializedName("resolution_last")
    @Expose
    private ResolutionLast resolutionLast;
    @SerializedName("resolution_order")
    @Expose
    private ResolutionOrder resolutionOrder;
    @SerializedName("resolution_by")
    @Expose
    private ResolutionBy resolutionBy;
    @SerializedName("resolution_shop")
    @Expose
    private ResolutionShop resolutionShop;
    @SerializedName("resolution_customer")
    @Expose
    private ResolutionCustomer resolutionCustomer;
    @SerializedName("resolution_dispute")
    @Expose
    private ResolutionDispute resolutionDispute;

    /**
     * 
     * @return
     *     The resolutionLast
     */
    public ResolutionLast getResolutionLast() {
        return resolutionLast;
    }

    /**
     * 
     * @param resolutionLast
     *     The resolution_last
     */
    public void setResolutionLast(ResolutionLast resolutionLast) {
        this.resolutionLast = resolutionLast;
    }

    /**
     * 
     * @return
     *     The resolutionOrder
     */
    public ResolutionOrder getResolutionOrder() {
        return resolutionOrder;
    }

    /**
     * 
     * @param resolutionOrder
     *     The resolution_order
     */
    public void setResolutionOrder(ResolutionOrder resolutionOrder) {
        this.resolutionOrder = resolutionOrder;
    }

    /**
     * 
     * @return
     *     The resolutionBy
     */
    public ResolutionBy getResolutionBy() {
        return resolutionBy;
    }

    /**
     * 
     * @param resolutionBy
     *     The resolution_by
     */
    public void setResolutionBy(ResolutionBy resolutionBy) {
        this.resolutionBy = resolutionBy;
    }

    /**
     * 
     * @return
     *     The resolutionShop
     */
    public ResolutionShop getResolutionShop() {
        return resolutionShop;
    }

    /**
     * 
     * @param resolutionShop
     *     The resolution_shop
     */
    public void setResolutionShop(ResolutionShop resolutionShop) {
        this.resolutionShop = resolutionShop;
    }

    /**
     * 
     * @return
     *     The resolutionCustomer
     */
    public ResolutionCustomer getResolutionCustomer() {
        return resolutionCustomer;
    }

    /**
     * 
     * @param resolutionCustomer
     *     The resolution_customer
     */
    public void setResolutionCustomer(ResolutionCustomer resolutionCustomer) {
        this.resolutionCustomer = resolutionCustomer;
    }

    /**
     * 
     * @return
     *     The resolutionDispute
     */
    public ResolutionDispute getResolutionDispute() {
        return resolutionDispute;
    }

    /**
     * 
     * @param resolutionDispute
     *     The resolution_dispute
     */
    public void setResolutionDispute(ResolutionDispute resolutionDispute) {
        this.resolutionDispute = resolutionDispute;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.resolutionLast, flags);
        dest.writeParcelable(this.resolutionOrder, flags);
        dest.writeParcelable(this.resolutionBy, flags);
        dest.writeParcelable(this.resolutionShop, flags);
        dest.writeParcelable(this.resolutionCustomer, flags);
        dest.writeParcelable(this.resolutionDispute, flags);
    }

    public ResolutionDetail() {
    }

    protected ResolutionDetail(Parcel in) {
        this.resolutionLast = in.readParcelable(ResolutionLast.class.getClassLoader());
        this.resolutionOrder = in.readParcelable(ResolutionOrder.class.getClassLoader());
        this.resolutionBy = in.readParcelable(ResolutionBy.class.getClassLoader());
        this.resolutionShop = in.readParcelable(ResolutionShop.class.getClassLoader());
        this.resolutionCustomer = in.readParcelable(ResolutionCustomer.class.getClassLoader());
        this.resolutionDispute = in.readParcelable(ResolutionDispute.class.getClassLoader());
    }

    public static final Parcelable.Creator<ResolutionDetail> CREATOR = new Parcelable.Creator<ResolutionDetail>() {
        @Override
        public ResolutionDetail createFromParcel(Parcel source) {
            return new ResolutionDetail(source);
        }

        @Override
        public ResolutionDetail[] newArray(int size) {
            return new ResolutionDetail[size];
        }
    };
}
