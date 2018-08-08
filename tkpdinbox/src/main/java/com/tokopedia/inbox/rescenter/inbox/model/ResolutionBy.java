package com.tokopedia.inbox.rescenter.inbox.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Generated;

@Generated("org.jsonschema2pojo")
public class ResolutionBy implements Parcelable {

    @SerializedName("user_label_id")
    @Expose
    private Integer userLabelId;
    @SerializedName("user_label")
    @Expose
    private String userLabel;
    @SerializedName("by_customer")
    @Expose
    private Integer byCustomer;
    @SerializedName("by_seller")
    @Expose
    private Integer bySeller;

    /**
     * 
     * @return
     *     The userLabelId
     */
    public Integer getUserLabelId() {
        return userLabelId;
    }

    /**
     * 
     * @param userLabelId
     *     The user_label_id
     */
    public void setUserLabelId(Integer userLabelId) {
        this.userLabelId = userLabelId;
    }

    /**
     * 
     * @return
     *     The userLabel
     */
    public String getUserLabel() {
        return userLabel;
    }

    /**
     * 
     * @param userLabel
     *     The user_label
     */
    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    /**
     * 
     * @return
     *     The byCustomer
     */
    public Integer getByCustomer() {
        return byCustomer;
    }

    /**
     * 
     * @param byCustomer
     *     The by_customer
     */
    public void setByCustomer(Integer byCustomer) {
        this.byCustomer = byCustomer;
    }

    /**
     * 
     * @return
     *     The bySeller
     */
    public Integer getBySeller() {
        return bySeller;
    }

    /**
     * 
     * @param bySeller
     *     The by_seller
     */
    public void setBySeller(Integer bySeller) {
        this.bySeller = bySeller;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.userLabelId);
        dest.writeString(this.userLabel);
        dest.writeValue(this.byCustomer);
        dest.writeValue(this.bySeller);
    }

    public ResolutionBy() {
    }

    protected ResolutionBy(Parcel in) {
        this.userLabelId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userLabel = in.readString();
        this.byCustomer = (Integer) in.readValue(Integer.class.getClassLoader());
        this.bySeller = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<ResolutionBy> CREATOR = new Parcelable.Creator<ResolutionBy>() {
        @Override
        public ResolutionBy createFromParcel(Parcel source) {
            return new ResolutionBy(source);
        }

        @Override
        public ResolutionBy[] newArray(int size) {
            return new ResolutionBy[size];
        }
    };
}
